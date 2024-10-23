'use client'

import { useState, useEffect, useRef } from 'react';
import { Menu, Search, Settings, X, Edit, Plus, RotateCcw, ChevronDown, Trash, Check } from 'lucide-react';
import TodoView from './TodoView';
import SearchResultsPage from './SearchResultsPage';
import SettingsPage from './SettingsPage';
import QuadrantsUI from './Quadrants';
import * as jcefBridge from './jcefBridge';
import { Todo as JcefTodo, List as JcefList } from './jcefBridge';

interface SortOption {
    label: string;
    value: 'importance' | 'dueDate' | 'alphabetical';
}

interface SortState {
    criteria: SortOption['value'];
    direction: 'asc' | 'desc';
}

export default function MobileTodoApp() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [activeTab, setActiveTab] = useState('home');
    const [tasks, setTasks] = useState<JcefTodo[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [lists, setLists] = useState<JcefList[]>([]);
    const [currentList, setCurrentList] = useState('我的一天');
    const [isCreatingList, setIsCreatingList] = useState(false);
    const [newListName, setNewListName] = useState('');
    const [editingListId, setEditingListId] = useState<number | null>(null);
    const [editListName, setEditListName] = useState('');
    const [sortState, setSortState] = useState<SortState>({
        criteria: 'importance',
        direction: 'desc',
    });
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        loadLists();
        loadTasks();
    }, []);

    const loadLists = async () => {
        try {
            const loadedLists = await jcefBridge.getAllLists();
            setLists(loadedLists);
        } catch (err) {
            setError('Failed to load lists: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };

    const loadTasks = async () => {
        try {
            setLoading(true);
            const loadedTasks = await jcefBridge.getAllTodos();
            setTasks(loadedTasks);
            setError(null);
        } catch (err) {
            setError('Failed to load tasks: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        } finally {
            setLoading(false);
        }
    };

    const handleAddTask = async (newTask: Omit<JcefTodo, 'id'>) => {
        try {
            const currentListId = lists.find((list) => list.name === currentList)?.id;
            if (!currentListId) throw new Error('Current list not found');

            const addedTask = await jcefBridge.addTodo({
                ...newTask,
                list_id: currentListId,
                list: currentList,
            });
            setTasks([...tasks, addedTask]);
        } catch (err) {
            setError('Failed to add task: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };

    const toggleComplete = async (id: number) => {
        try {
            const taskToUpdate = tasks.find((task) => task.id === id);
            if (taskToUpdate) {
                const updatedTask = await jcefBridge.updateTodo({
                    ...taskToUpdate,
                    completed: !taskToUpdate.completed,
                });
                setTasks(tasks.map((task) => (task.id === id ? updatedTask : task)));
            }
        } catch (err) {
            setError('Failed to update task: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };

    const toggleImportant = async (id: number) => {
        try {
            const taskToUpdate = tasks.find((task) => task.id === id);
            if (taskToUpdate) {
                const updatedTask = await jcefBridge.updateTodo({
                    ...taskToUpdate,
                    importance: taskToUpdate.importance === 100 ? 0 : 100,
                });
                setTasks(tasks.map((task) => (task.id === id ? updatedTask : task)));
            }
        } catch (err) {
            setError('Failed to update task: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };

    const handleSortChange = async (criteria: SortOption['value']) => {
        try {
            const newDirection = sortState.criteria === criteria && sortState.direction === 'desc' ? 'asc' : 'desc';
            const sortedTasks = await jcefBridge.sortTodos(criteria, newDirection);
            setTasks(sortedTasks);
            setSortState({ criteria, direction: newDirection });
        } catch (err) {
            setError('Failed to sort tasks: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };

    const handleListClick = (listName: string) => {
        setCurrentList(listName);
        setIsSidebarOpen(false);
    };

    const handleCreateList = async () => {
        if (newListName.trim()) {
            if (lists.some((list) => list.name.toLowerCase() === newListName.trim().toLowerCase())) {
                alert('该列表名称已存在，请选择其他名称。');
                return;
            }
            try {
                const newList = await jcefBridge.addList({
                    name: newListName.trim(),
                    icon: '•',
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

    const handleCancelCreate = () => {
        setIsCreatingList(false);
        setNewListName('');
    };

    const handleEditList = (list: JcefList) => {
        if (list.isDefault) return;
        setEditingListId(list.id);
        setEditListName(list.name);
    };

    const handleUpdateList = async () => {
        if (editListName.trim() && editingListId !== null) {
            if (lists.some((list) => list.id !== editingListId && list.name.toLowerCase() === editListName.trim().toLowerCase())) {
                alert('该列表名称已存在，请选择其他名称。');
                return;
            }
            try {
                const updatedList = await jcefBridge.updateList({
                    id: editingListId,
                    name: editListName.trim(),
                    icon: '•',
                });
                setLists(lists.map((list) => (list.id === editingListId ? updatedList : list)));
                setEditingListId(null);
                setEditListName('');
                setCurrentList(updatedList.name);
            } catch (err) {
                setError('Failed to update list: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
            }
        }
    };

    const handleDeleteList = async (listId: number) => {
        const listToDelete = lists.find((l) => l.id === listId);
        if (!listToDelete || listToDelete.isDefault) return;

        try {
            await jcefBridge.deleteList(listId);
            // 删除该列表下的所有任务
            await Promise.all(tasks.filter((task) => task.list_id === listId).map((task) => jcefBridge.deleteTodo(task.id)));
            setLists(lists.filter((list) => list.id !== listId));
            setTasks(tasks.filter((task) => task.list_id !== listId));

            if (currentList === listToDelete.name) {
                setCurrentList('我的一天');
            }
        } catch (err) {
            setError('Failed to delete list: ' + (err instanceof Error ? err.message : 'An unknown error occurred'));
        }
    };

    const getListCount = (listName: string): number => {
        return tasks.filter((task) => task.list === listName).length;
    };

    const sortButtonRef = useRef<HTMLButtonElement>(null);
    const sortMenuRef = useRef<HTMLDivElement>(null);
    const [isSortMenuOpen, setIsSortMenuOpen] = useState(false);
    const [sortMenuPosition, setSortMenuPosition] = useState({ top: 0, left: 0 });

    const handleSortClick = (event: React.MouseEvent) => {
        event.stopPropagation();
        const rect = event.currentTarget.getBoundingClientRect();
        setSortMenuPosition({ top: rect.bottom, left: rect.left });
        setIsSortMenuOpen(!isSortMenuOpen);
    };

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (
                sortMenuRef.current &&
                !sortMenuRef.current.contains(event.target as Node) &&
                sortButtonRef.current &&
                !sortButtonRef.current.contains(event.target as Node)
            ) {
                setIsSortMenuOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleSortOptionSelect = async (option: SortOption['value'], event: React.MouseEvent) => {
        event.stopPropagation();
        try {
            await handleSortChange(option);
        } catch (err) {
            console.error('Failed to sort tasks:', err);
        }
        setIsSortMenuOpen(false);
    };

    const getSortLabel = () => {
        const labels = {
            importance: '重要性',
            dueDate: '到期日',
            alphabetical: '字母顺序',
        };
        return `${labels[sortState.criteria]} ${sortState.direction === 'asc' ? '↑' : '↓'}`;
    };

    return (
        <div className="h-screen flex flex-col bg-gray-100">
            {/* Header */}
            <header className="bg-blue-600 text-white p-4 flex items-center justify-between">
                <button onClick={() => setIsSidebarOpen(true)} className="text-white">
                    <Menu size={24} />
                </button>
                <h1 className="text-xl font-bold">{currentList}</h1>
                <div className="flex items-center">
                    <button
                        ref={sortButtonRef}
                        className="p-1 flex items-center"
                        onClick={handleSortClick}
                    >
                        <RotateCcw size={20} />
                        <ChevronDown size={16} className="ml-1" />
                    </button>
                    {isSortMenuOpen && (
                        <div
                            ref={sortMenuRef}
                            className="absolute bg-white border rounded shadow-lg"
                            style={{ top: `${sortMenuPosition.top}px`, left: `${sortMenuPosition.left}px` }}
                        >
                            <button
                                className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                                onClick={(e) => handleSortOptionSelect('importance', e)}
                            >
                                按重要性 {sortState.criteria === 'importance' && (sortState.direction === 'asc' ? '↑' : '↓')}
                            </button>
                            <button
                                className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                                onClick={(e) => handleSortOptionSelect('dueDate', e)}
                            >
                                按到期日 {sortState.criteria === 'dueDate' && (sortState.direction === 'asc' ? '↑' : '↓')}
                            </button>
                            <button
                                className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                                onClick={(e) => handleSortOptionSelect('alphabetical', e)}
                            >
                                按字母顺序 {sortState.criteria === 'alphabetical' && (sortState.direction === 'asc' ? '↑' : '↓')}
                            </button>
                        </div>
                    )}
                </div>
            </header>

            {/* Search Bar */}
            <div className="bg-blue-600 pb-4 px-4">
                <div className="bg-white rounded-full flex items-center px-4 py-2">
                    <Search size={20} className="text-gray-500" />
                    <input
                        type="text"
                        placeholder="搜索"
                        className="ml-2 w-full outline-none text-sm"
                        onFocus={() => setActiveTab('search')}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        value={searchQuery}
                    />
                </div>
            </div>

            {/* Main Content */}
            <main className="flex-1 overflow-y-auto p-4">
                {loading ? (
                    <div className="flex items-center justify-center h-full text-gray-500">加载中...</div>
                ) : error ? (
                    <div className="flex items-center justify-center h-full text-red-500">{error}</div>
                ) : activeTab === 'home' ? (
                    <>
                        {/* 集成 QuadrantsUI 组件 */}
                        <div className="w-full flex justify-center mb-4">
                            <div className="relative" style={{ width: '100%', maxWidth: '400px' }}>
                                <QuadrantsUI />
                            </div>
                        </div>
                        {/* 任务列表 */}
                        <TodoView
                            tasks={tasks.filter((task) => task.list === currentList)}
                            currentList={currentList}
                            onAddTask={handleAddTask}
                            onToggleComplete={toggleComplete}
                            onToggleImportant={toggleImportant}
                            onSortChange={handleSortChange}
                            sortState={sortState}
                            lists={lists}
                        />
                    </>
                ) : activeTab === 'search' ? (
                    <SearchResultsPage
                        initialQuery={searchQuery}
                        onBack={() => setActiveTab('home')}
                    />
                ) : activeTab === 'settings' ? (
                    <SettingsPage
                        onCancel={() => setActiveTab('home')}
                        onSave={() => setActiveTab('home')}
                        onSettingsChange={() => { }}
                    />
                ) : null}
            </main>

            {/* Bottom Navigation */}
            <nav className="bg-white border-t border-gray-200">
                <div className="flex justify-around">
                    {['home', 'search', 'settings'].map((tab) => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={`p-4 flex flex-col items-center ${activeTab === tab ? 'text-blue-600' : 'text-gray-400'
                                }`}
                        >
                            {tab === 'home' && (
                                <div className="w-6 h-6 grid grid-cols-2 gap-0.5">
                                    <div className="bg-current rounded-tl-sm"></div>
                                    <div className="bg-current rounded-tr-sm"></div>
                                    <div className="bg-current rounded-bl-sm"></div>
                                    <div className="bg-current rounded-br-sm"></div>
                                </div>
                            )}
                            {tab === 'search' && <Search size={24} />}
                            {tab === 'settings' && <Settings size={24} />}
                            <span className="mt-1 text-xs">{tab === 'home' ? '主页' : tab === 'search' ? '搜索' : '设置'}</span>
                        </button>
                    ))}
                </div>
            </nav>

            {/* Sidebar */}
            <div
                className={`fixed inset-0 bg-black bg-opacity-50 z-50 transition-opacity duration-300 ease-in-out ${isSidebarOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
                    }`}
                onClick={() => setIsSidebarOpen(false)}
            >
                <div
                    className={`bg-white h-full w-[70%] shadow-lg transition-transform duration-300 ease-in-out ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'
                        }`}
                    onClick={(e) => e.stopPropagation()}
                >
                    <div className="p-4 flex justify-between items-center bg-blue-600 text-white">
                        <h2 className="text-xl font-bold">To Do</h2>
                        <button onClick={() => setIsSidebarOpen(false)}>
                            <X size={24} />
                        </button>
                    </div>
                    {/* Sidebar content */}
                    <nav className="mt-4 flex-1">
                        {lists.map((list) => (
                            <div key={list.id} className="relative group">
                                {editingListId === list.id ? (
                                    <div className="px-4 py-2">
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
                                                onClick={handleUpdateList}
                                                className="ml-2 text-gray-500 hover:text-green-600"
                                            >
                                                <Check size={16} />
                                            </button>
                                            <button
                                                type="button"
                                                onClick={() => setEditingListId(null)}
                                                className="ml-2 text-gray-500 hover:text-gray-700"
                                            >
                                                <X size={16} />
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <div
                                        className={`flex items-center justify-between px-4 py-2 hover:bg-gray-100 ${currentList === list.name ? 'bg-gray-100' : ''
                                            }`}
                                    >
                                        <div
                                            className="flex items-center flex-grow cursor-pointer"
                                            onClick={() => handleListClick(list.name)}
                                        >
                                            <span className="mr-2 flex-shrink-0">{list.icon}</span>
                                            <span className="flex-grow">{list.name}</span>
                                        </div>
                                        <div className="flex items-center">
                                            {!list.isDefault && (
                                                <div className="flex items-center">
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
                                        className="flex-1 bg-transparent focus:outline-none"
                                        autoFocus
                                    />
                                    <button
                                        type="button"
                                        onClick={handleCreateList}
                                        className="ml-2 text-gray-500 hover:text-green-600"
                                    >
                                        <Check size={16} />
                                    </button>
                                    <button
                                        type="button"
                                        onClick={handleCancelCreate}
                                        className="ml-2 text-gray-500 hover:text-gray-700"
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
    );
}
