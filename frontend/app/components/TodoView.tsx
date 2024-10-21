import React, {useEffect, useRef, useState} from 'react';
import {ChevronDown, RotateCcw} from 'lucide-react';
import {List as JcefList, Todo as JcefTodo} from './jcefBridge';

interface TodoViewProps {
    tasks: JcefTodo[];
    currentList: string;
    onAddTask: (task: Omit<JcefTodo, 'id'>) => void;
    onToggleComplete: (id: number) => void;
    onToggleImportant: (id: number) => void;
    onSortChange: (criteria: SortOption['value']) => void;
    sortState: SortState;
    lists: JcefList[]; // Add this line
}


interface SortOption {
    label: string;
    value: 'importance' | 'dueDate' | 'alphabetical';
}

interface SortState {
    criteria: SortOption['value'];
    direction: 'asc' | 'desc';
}

const TodoView: React.FC<TodoViewProps> = ({
                                               tasks,
                                               currentList,
                                               onAddTask,
                                               onToggleComplete,
                                               onSortChange,
                                               sortState,
                                               lists // Add this line
                                           }) => {
    const [newTask, setNewTask] = useState({
        title: '',
        dueDate: '',
        importance: 50
    });

    const [isSortMenuOpen, setIsSortMenuOpen] = useState(false);
    const [sortMenuPosition, setSortMenuPosition] = useState({top: 0, left: 0});

    const sortButtonRef = useRef<HTMLButtonElement>(null);
    const sortMenuRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (sortMenuRef.current && !sortMenuRef.current.contains(event.target as Node) &&
                sortButtonRef.current && !sortButtonRef.current.contains(event.target as Node)) {
                setIsSortMenuOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleSortClick = (event: React.MouseEvent) => {
        event.stopPropagation();
        const rect = event.currentTarget.getBoundingClientRect();
        setSortMenuPosition({top: rect.bottom, left: rect.left});
        setIsSortMenuOpen(!isSortMenuOpen);
    };


    const addTask = async (e: React.FormEvent) => {
        e.preventDefault();
        if (newTask.title.trim()) {
            try {
                const currentListId = lists.find(list => list.name === currentList)?.id;
                if (!currentListId) throw new Error('Current list not found');

                await onAddTask({
                    title: newTask.title,
                    completed: false,
                    dueDate: newTask.dueDate,
                    importance: newTask.importance,
                    list: currentList,
                    list_id: currentListId
                });
                setNewTask({title: '', dueDate: '', importance: 50});
            } catch (err) {
                console.error('Failed to add task:', err);
            }
        }
    };

    const handleToggleComplete = async (id: number) => {
        try {
            await onToggleComplete(id);
        } catch (err) {
            console.error('Failed to toggle task completion:', err);
            // You might want to show an error message to the user here
        }
    };

    const handleSortOptionSelect = async (option: SortOption['value'], event: React.MouseEvent) => {
        event.stopPropagation();
        try {
            await onSortChange(option);
        } catch (err) {
            console.error('Failed to sort tasks:', err);
            // You might want to show an error message to the user here
        }
        setIsSortMenuOpen(false);
    };

    const getSortLabel = () => {
        const labels = {
            importance: '重要性',
            dueDate: '到期日',
            alphabetical: '字母顺序'
        };
        return `${labels[sortState.criteria]} ${sortState.direction === 'asc' ? '↑' : '↓'}`;
    };

    return (
        <>
            <div className="bg-white p-4 flex items-center justify-between border-b">
                <h3 className="text-xl font-semibold">{currentList}</h3>
                <div className="flex items-center">
                    <button className="p-2"><RotateCcw size={20}/></button>
                    <button
                        ref={sortButtonRef}
                        className="p-2 flex items-center"
                        onClick={handleSortClick}
                    >
                        排序 ({getSortLabel()}) <ChevronDown size={16} className="ml-1"/>
                    </button>
                    {isSortMenuOpen && (
                        <div
                            ref={sortMenuRef}
                            className="absolute bg-white border rounded shadow-lg"
                            style={{top: `${sortMenuPosition.top}px`, left: `${sortMenuPosition.left}px`}}
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
            </div>
            <div className="flex-1 overflow-auto p-4">
                {tasks.map(task => (
                    <div key={task.id} className="flex items-center mb-4 bg-white p-4 rounded-lg shadow">
                        <input
                            type="checkbox"
                            checked={task.completed}
                            onChange={() => handleToggleComplete(task.id)}
                            className="mr-4"
                        />
                        <span className={task.completed ? 'line-through text-gray-500' : ''}>{task.title}</span>
                        <div className="ml-auto flex items-center gap-4">
                            <div className="flex items-center">
                                <span className="text-sm text-gray-500 mr-2">重要性:</span>
                                <span className="font-medium">{task.importance}</span>
                            </div>
                            <span className="text-sm text-gray-500">{task.dueDate}</span>
                        </div>
                    </div>
                ))}
            </div>
            <form onSubmit={addTask} className="bg-white p-4 border-t">
                <div className="flex flex-col space-y-4">
                    <input
                        type="text"
                        value={newTask.title}
                        onChange={(e) => setNewTask({...newTask, title: e.target.value})}
                        placeholder="任务名称"
                        className="p-2 border-b border-gray-300 focus:outline-none focus:border-blue-500"
                    />
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-4">
                            <input
                                type="date"
                                value={newTask.dueDate}
                                onChange={(e) => setNewTask({...newTask, dueDate: e.target.value})}
                                className="p-2 border-b border-gray-300 focus:outline-none focus:border-blue-500"
                            />
                            <div className="flex items-center">
                                <span className="mr-2">重要性 (0-100):</span>
                                <input
                                    type="number"
                                    min="0"
                                    max="100"
                                    value={newTask.importance}
                                    onChange={(e) => setNewTask({
                                        ...newTask,
                                        importance: Math.min(100, Math.max(0, parseInt(e.target.value) || 0))
                                    })}
                                    className="w-20 p-2 border-b border-gray-300 focus:outline-none focus:border-blue-500"
                                />
                            </div>
                        </div>
                        <button
                            type="submit"
                            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 focus:outline-none"
                        >
                            新建任务
                        </button>
                    </div>
                </div>
            </form>
        </>
    );

};

export default TodoView;