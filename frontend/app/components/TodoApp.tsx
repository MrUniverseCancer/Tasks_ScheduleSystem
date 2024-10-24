'use client';

import React, {useEffect, useState} from 'react';
import {Check, Edit, Menu, Plus, Search, Settings, Trash, X} from 'lucide-react';
import SettingsPage from './SettingsPage';
import TodoView from './TodoView';
import QuadrantsUI from './Quadrants';
import SearchBox from './SearchBox';
import SearchResultsPage from './SearchResultsPage';
import * as jcefBridge from './jcefBridge';
import {Todo as JcefTodo} from './jcefBridge';


interface ViewState {
    currentView: 'todo' | 'settings' | 'quadrants' | 'searchResults';
    previousList: string;
}

type List = Omit<jcefBridge.List, 'icon'> & {
    icon: string | JSX.Element;
};

interface SortOption {
    label: string;
    value: 'importance' | 'dueDate' | 'alphabetical';
}

interface SortState {
    criteria: SortOption['value'];
    direction: 'asc' | 'desc';
}

export default function TodoApp() {
    const [allTasks, setAllTasks] = useState<JcefTodo[]>([]);
    const [tasks, setTasks] = useState<JcefTodo[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [sidebarOpen, setSidebarOpen] = useState(true)
    const [currentList, setCurrentList] = useState('我的一天')
    const [isCreatingList, setIsCreatingList] = useState(false)
    const [newListName, setNewListName] = useState('')
    const [editingListId, setEditingListId] = useState<number | null>(null)
    const [editListName, setEditListName] = useState('')
    const [sortState, setSortState] = useState<SortState>({
        criteria: 'importance',
        direction: 'desc'
    })
    const [viewState, setViewState] = useState<ViewState>({
        currentView: 'quadrants',
        previousList: '我的一天'
    });
    const [searchQuery, setSearchQuery] = useState('');
    const [hasUnsavedSettings, setHasUnsavedSettings] = useState(false);
    const [lists, setLists] = useState<List[]>([]);

    useEffect(() => {
        loadLists();
        loadTasks();
    }, []);

    useEffect(() => {
        const selectedList = lists.find(list => list.name === currentList);
        if (selectedList) {
            setTasks(allTasks.filter(task => task.list_id === selectedList.id));
        }
    }, [currentList, allTasks]);


    const loadLists = async () => {
        try {
            const loadedLists = await jcefBridge.getAllLists();
            setLists(loadedLists);
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to load lists: ' + err.message);
            } else {
                setError('Failed to load lists: An unknown error occurred');
            }
        }
    };

    const loadTasks = async () => {
        try {
            setLoading(true);
            const loadedTasks = await jcefBridge.getAllTodos();
            setAllTasks(loadedTasks);
            const selectedList = lists.find(list => list.name === currentList);
            if (selectedList) {
                setTasks(loadedTasks.filter(task => task.list_id === selectedList.id));
            } else {
                setTasks(loadedTasks);
            }
            setError(null);
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to load tasks: ' + err.message);
            } else {
                setError('Failed to load tasks: An unknown error occurred');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleAddTask = async (newTask: Omit<JcefTodo, 'id'>) => {
        try {
            const currentListId = lists.find(list => list.name === currentList)?.id;
            if (!currentListId) throw new Error('Current list not found');

            const addedTask = await jcefBridge.addTodo({
                ...newTask,
                list_id: currentListId,
                list: currentList
            });
            setAllTasks([...allTasks, addedTask]);
            if (addedTask.list === currentList) {
                setTasks([...tasks, addedTask]);
            }
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to add task: ' + err.message);
            } else {
                setError('Failed to add task: An unknown error occurred');
            }
        }
    };

    const toggleComplete = async (id: number) => {
        try {
            const taskToUpdate = allTasks.find(task => task.id === id);
            if (taskToUpdate) {
                const updatedTask = await jcefBridge.updateTodo({
                    ...taskToUpdate,
                    completed: !taskToUpdate.completed
                });
                setAllTasks(allTasks.map(task => task.id === id ? updatedTask : task));
                if (updatedTask.list === currentList) {
                    setTasks(tasks.map(task => task.id === id ? updatedTask : task));
                }
            }
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to update task: ' + err.message);
            } else {
                setError('Failed to update task: An unknown error occurred');
            }
        }
    };

    const toggleImportant = async (id: number) => {
        try {
            const taskToUpdate = tasks.find(task => task.id === id);
            if (taskToUpdate) {
                const updatedTask = await jcefBridge.updateTodo({
                    ...taskToUpdate,
                    importance: taskToUpdate.importance === 100 ? 0 : 100
                });
                setTasks(tasks.map(task => task.id === id ? updatedTask : task));
            }
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to update task: ' + err.message);
            } else {
                setError('Failed to update task: An unknown error occurred');
            }
        }
    };

    const handleSortChange = async (criteria: SortOption['value']) => {
        try {
            const newDirection = sortState.criteria === criteria && sortState.direction === 'desc' ? 'asc' : 'desc';
            const sortedAllTasks = await jcefBridge.sortTodos(criteria, newDirection);
            setAllTasks(sortedAllTasks);
            setSortState({ criteria, direction: newDirection });

            // 更新当前显示的任务列表
            const selectedList = lists.find(list => list.name === currentList);
            if (selectedList) {
                setTasks(sortedAllTasks.filter(task => task.list_id === selectedList.id));
            }
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to sort tasks: ' + err.message);
            } else {
                setError('Failed to sort tasks: An unknown error occurred');
            }
        }
    };

    const handleCreateList = async () => {
        if (newListName.trim()) {
            if (lists.some(list => list.name.toLowerCase() === newListName.trim().toLowerCase())) {
                alert('A list with this name already exists. Please choose a different name.');
                return;
            }
            try {
                const newList = await jcefBridge.addList({
                    name: newListName.trim(),
                    icon: '•'
                });
                setLists([...lists, newList]);
                setNewListName('');
                setIsCreatingList(false);
                setCurrentList(newList.name);
            } catch (err) {
                setError('Failed to create list: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
            }
        }
    };

    const handleUpdateList = async (e: React.FormEvent) => {
        e.preventDefault();
        if (editListName.trim() && editingListId) {
            if (lists.some(list => list.id !== editingListId && list.name.toLowerCase() === editListName.trim().toLowerCase())) {
                alert('A list with this name already exists. Please choose a different name.');
                return;
            }
            try {
                const listToUpdate = lists.find(l => l.id === editingListId);
                if (!listToUpdate) throw new Error('List not found');

                const updatedList = await jcefBridge.updateList({
                    id: editingListId,
                    name: editListName.trim(),
                    icon: typeof listToUpdate.icon === 'string' ? listToUpdate.icon : '•'
                });
                setLists(lists.map(list => list.id === editingListId ? { ...updatedList, icon: list.icon } : list));
                setEditingListId(null);
                setEditListName('');
                setCurrentList(updatedList.name);
            } catch (err) {
                setError('Failed to update list: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
            }
        }
    };

    const handleDeleteList = async (listId: number) => {
        const listToDelete = lists.find(l => l.id === listId);
        if (!listToDelete || listToDelete.isDefault) return;

        try {
            await jcefBridge.deleteList(listId);
            await Promise.all(tasks.filter(task => task.list_id === listId).map(task => jcefBridge.deleteTodo(task.id)));
            setLists(lists.filter(list => list.id !== listId));
            setTasks(tasks.filter(task => task.list_id !== listId));

            if (currentList === listToDelete.name) {
                setCurrentList('我的一天');
            }
        } catch (err) {
            setError('Failed to delete list: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };


    const handleEditList = (list: List) => {
        if (list.isDefault) return;
        setEditingListId(list.id);
        setEditListName(list.name);
    }


    const handleCancelCreate = () => {
        setIsCreatingList(false)
        setNewListName('')
    }


    const toggleSidebar = () => {
        setSidebarOpen(!sidebarOpen)
    }

    const getListCount = (listName: string): number => {
        return allTasks.filter(task => task.list === listName).length;
    }


    const handleSettingsClick = () => {
        setViewState(() => ({
            currentView: 'settings',
            previousList: currentList
        }));
    };

    const handleSettingsCancel = () => {
        if (hasUnsavedSettings) {
            if (window.confirm('You have unsaved changes. Are you sure you want to cancel?')) {
                setViewState(prevState => ({
                    currentView: 'todo',
                    previousList: prevState.previousList
                }));
                setCurrentList(viewState.previousList);
                setHasUnsavedSettings(false);
            }
        } else {
            setViewState(prevState => ({
                currentView: 'todo',
                previousList: prevState.previousList
            }));
            setCurrentList(viewState.previousList);
        }
    };

    const handleSettingsSave = () => {
        // Implement your save logic here
        setViewState(prevState => ({
            currentView: 'todo',
            previousList: prevState.previousList
        }));
        setCurrentList(viewState.previousList);
        setHasUnsavedSettings(false);
    };

    const handleListClick = (listName: string) => {
        if (viewState.currentView === 'settings' && hasUnsavedSettings) {
            if (window.confirm('You have unsaved settings. Do you want to save before switching to the todo view?')) {
                handleSettingsSave();
            } else {
                handleSettingsCancel();
            }
        }
        setCurrentList(listName);
        setViewState(prevState => ({
            ...prevState,
            currentView: 'todo'
        }));
        const selectedList = lists.find(list => list.name === listName);
        if (selectedList) {
            setTasks(allTasks.filter(task => task.list_id === selectedList.id));
        }
    };

    const handleSettingsChange = () => {
        setHasUnsavedSettings(true);
    };


    const handleQuadrantsClick = () => {
        setViewState(() => ({
            currentView: 'quadrants',
            previousList: currentList
        }));
    };

    const handleSearchViewMore = () => {
        setViewState((prevState) => ({
            ...prevState,
            currentView: 'searchResults',
        }));
    };

    const handleSearchSubmit = (query: string) => {
        setSearchQuery(query);
        setViewState((prevState) => ({
            ...prevState,
            currentView: 'searchResults',
        }));
    };

    const handleBackFromSearch = () => {
        setViewState((prevState) => ({
            ...prevState,
            currentView: 'todo',
        }));
        setSearchQuery('');
    };

    return (
        <div className="flex h-screen bg-gray-100">
            <div
                className={`relative transition-all duration-300 ease-in-out ${sidebarOpen ? 'w-64' : 'w-0'} overflow-hidden`}>
                <div className="absolute top-0 left-0 w-64 h-full bg-white border-r">
                    <div className="flex flex-col h-full">
                        <div className="p-4">
                            <h1 className="text-2xl font-semibold text-blue-600 whitespace-nowrap">To Do</h1>
                        </div>
                        <nav className="mt-4 flex-1">
                            {lists.map((list) => (
                                <div key={list.id} className="relative group">
                                    {editingListId === list.id ? (
                                        <form onSubmit={handleUpdateList} className="px-4 py-2">
                                            <div className="flex items-center bg-gray-100 rounded-lg p-2">
                                                <input
                                                    type="text"
                                                    value={editListName}
                                                    onChange={(e) => setEditListName(e.target.value)}
                                                    className="flex-1 bg-transparent focus:outline-none"
                                                    autoFocus
                                                />
                                                <button
                                                    type="button"
                                                    onClick={() => setEditingListId(null)}
                                                    className="ml-2 text-gray-500 hover:text-gray-700"
                                                >
                                                    <X size={16} />
                                                </button>
                                            </div>
                                        </form>
                                    ) : (
                                        <div
                                            className={`flex items-center justify-between px-4 py-2 hover:bg-gray-100 
                                    ${currentList === list.name && viewState.currentView === 'todo' && list.id === lists.find(l => l.name === currentList)?.id
                                                ? 'bg-gray-100 relative after:absolute after:right-0 after:top-0 after:bottom-0 after:w-1 after:bg-gray-700'
                                                : ''}`}
                                        >
                                            <div
                                                className="flex items-center flex-grow cursor-pointer"
                                                onClick={() => handleListClick(list.name)}
                                            >
                                            <span className="mr-2 flex-shrink-0">
                                                {list.icon}
                                            </span>
                                                <span className="flex-grow">{list.name}</span>
                                            </div>
                                            <div className="flex items-center">
                                                {!list.isDefault && (
                                                    <div className="flex items-center mr-2">
                                                        <button
                                                            onClick={() => handleEditList(list)}
                                                            className="p-1 text-gray-500 hover:text-blue-600"
                                                        >
                                                            <Edit size={16} />
                                                        </button>
                                                        <button
                                                            onClick={() => handleDeleteList(list.id)}
                                                            className="p-1 text-gray-500 hover:text-red-600"
                                                        >
                                                            <Trash size={16} />
                                                        </button>
                                                    </div>
                                                )}
                                                <span className="ml-auto">
                                                {getListCount(list.name)}
                                            </span>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            ))}
                        </nav>
                        <div className="p-4 border-t">
                            {isCreatingList ? (
                                <div className="px-4 py-2">
                                    <div className="flex items-center bg-gray-100 rounded-lg p-2">
                                        <input
                                            type="text"
                                            value={newListName}
                                            onChange={(e) => setNewListName(e.target.value)}
                                            placeholder="列表名称"
                                            className="flex-grow flex-shrink min-w-0 bg-transparent focus:outline-none"
                                            autoFocus
                                        />
                                        <button
                                            type="button"
                                            onClick={handleCreateList}
                                            className="ml-2 text-gray-500 hover:text-green-600 flex-shrink-0"
                                        >
                                            <Check size={16} />
                                        </button>
                                        <button
                                            type="button"
                                            onClick={handleCancelCreate}
                                            className="ml-2 text-gray-500 hover:text-green-600 flex-shrink-0"
                                        >
                                            <X size={16} />
                                        </button>
                                    </div>
                                </div>
                            ) : (
                                <button
                                    onClick={() => setIsCreatingList(true)}
                                    className="flex items-center text-blue-600 hover:bg-gray-100 w-full px-4 py-2 rounded-lg whitespace-nowrap"
                                >
                                    <Plus size={20} className="mr-2 flex-shrink-0" />
                                    <span>新建列表</span>
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            <div className="flex-1 flex flex-col">
                <header className="bg-blue-600 text-white p-4 flex items-center justify-between">
                    <div className="flex items-center">
                        <button
                            onClick={toggleSidebar}
                            className="hover:bg-blue-700 p-1 rounded"
                        >
                            <Menu className="mr-4" />
                        </button>
                        <h2 className="text-xl font-semibold">To Do</h2>
                    </div>
                    <SearchBox
                        onSearch={handleSearchSubmit}
                        onViewMore={handleSearchViewMore}
                        searchQuery={searchQuery}
                        setSearchQuery={setSearchQuery}
                        isSearchResultsPage={viewState.currentView === 'searchResults'}
                    />
                    <div className="flex items-center">
                        <button
                            onClick={handleQuadrantsClick}
                            className={`mr-4 p-1 rounded-t-lg transition-all ${viewState.currentView === 'quadrants'
                                ? 'bg-white -mb-1 px-3 py-2 text-blue-600'
                                : 'hover:bg-blue-700 text-white'
                            }`}
                        >
                            <div className={`grid grid-cols-2 gap-0.5`}>
                                <div className="w-2 h-2 bg-current rounded-sm"></div>
                                <div className="w-2 h-2 bg-current rounded-sm"></div>
                                <div className="w-2 h-2 bg-current rounded-sm"></div>
                                <div className="w-2 h-2 bg-current rounded-sm"></div>
                            </div>
                        </button>
                        <button
                            onClick={handleSettingsClick}
                            className={`mr-4 p-1 rounded-t-lg transition-all ${viewState.currentView === 'settings'
                                ? 'bg-white -mb-1 px-3 py-2 text-blue-600'
                                : 'hover:bg-blue-700 text-white'
                            }`}
                        >
                            <Settings size={20} />
                        </button>
                        <button
                            onClick={() => handleSearchSubmit(searchQuery)}
                            className={`mr-4 p-1 rounded-t-lg transition-all ${viewState.currentView === 'searchResults'
                                ? 'bg-white -mb-1 px-3 py-2 text-blue-600'
                                : 'hover:bg-blue-700 text-white'
                            }`}
                        >
                            <Search size={20} />
                        </button>
                        <div className="w-8 h-8 rounded-full bg-pink-500 flex items-center justify-center ml-2">
                            <span className="text-white font-bold">U</span>
                        </div>
                    </div>
                </header>

                {loading ? (
                    <div className="flex-1 flex items-center justify-center">
                        <p>Loading tasks...</p>
                    </div>
                ) : error ? (
                    <div className="flex-1 flex items-center justify-center">
                        <p className="text-red-500">{error}</p>
                    </div>
                ) : (
                    <>
                        {viewState.currentView === 'todo' ? (
                            <TodoView
                                tasks={tasks.filter((task) => task.list === currentList)}
                                currentList={currentList}
                                onAddTask={handleAddTask}
                                onToggleComplete={toggleComplete}
                                onToggleImportant={toggleImportant}
                                onSortChange={handleSortChange}
                                sortState={sortState}
                                lists={lists as jcefBridge.List[]}
                            />
                        ) : viewState.currentView === 'settings' ? (
                            <SettingsPage
                                onCancel={handleSettingsCancel}
                                onSave={handleSettingsSave}
                                onSettingsChange={handleSettingsChange}
                            />
                        ) : viewState.currentView === 'searchResults' ? (
                            <SearchResultsPage
                                searchQuery={searchQuery}
                                onBack={handleBackFromSearch}
                            />
                        ) : (
                            <QuadrantsUI />
                        )}
                    </>
                )}
            </div>
        </div>
    );

}
