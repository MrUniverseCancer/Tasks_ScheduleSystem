"use client"

import {CSSProperties, useEffect, useState} from "react"
import {AlertCircle, CalendarDays, CheckCircle2, ListTodo, X} from "lucide-react"
import * as jcefBridge from './jcefBridge';

interface Task {
    id: number;
    title: string;
    completed: boolean;
    dueDate: string;
    importance: number;
    list: string;
}


export default function QuadrantsUI() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [selectedTask, setSelectedTask] = useState<Task | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        loadTasks();
    }, []);

    const loadTasks = async () => {
        try {
            setLoading(true);
            const loadedTasks = await jcefBridge.getAllTodos();
            setTasks(loadedTasks);
            setError(null);
        } catch (err) {
            if (err instanceof Error) {
                setError('Failed to load tasks. ' + err.message);
            } else {
                setError('Failed to load tasks. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    };


    const getTaskPosition = (task: Task): CSSProperties => {
        // Calculate urgency based on due date
        const now = new Date().getTime()
        const due = new Date(task.dueDate).getTime()
        const timeUntilDue = due - now
        const maxTimeFrame = 1000 * 60 * 60 * 24 * 30 // 30 days
        const urgency = Math.max(0, Math.min(100, (1 - timeUntilDue / maxTimeFrame) * 100))

        // 计算颜色和大小
        const size = 30 + ((urgency > 50) ? urgency * 0.2 : 0); // 根据紧迫度设置大小，范围从20到30
        const color = `hsl(${(1 - task.importance / 100) * 120}, 100%, 50%)`; // 绿色到红色的渐变

        // // Get quadrant color
        // let color = ""
        // if (task.importance >= 50 && urgency >= 50) {
        //     color = "#dc2626"  // bg-red-600
        // } else if (task.importance >= 50 && urgency < 50) {
        //     color = "#2563eb" // bg-blue-600
        // } else if (task.importance < 50 && urgency >= 50) {
        //     color = "#ca8a04" // bg-yellow-600
        // } else {
        //     color = "#16a34a" // bg-green-600
        // }

        return {
            position: "absolute" as const,
            right: `${(urgency)}%`,
            bottom: `${task.importance}%`,
            transform: "translate(50%, 50%)",
            width:  `${size}px`,
            height: `${size}px`,
            backgroundColor: color
        }
    }
    // const handleTaskClick = async (task: Task) => {
    //     try {
    //         const updatedTask = await jcefBridge.getTodo(task.id);
    //         setSelectedTask(updatedTask);
    //     } catch (err) {
    //         console.error('Failed to get task details:', err);
    //         setError('Failed to load task details. Please try again.');
    //     }
    // };

    // const handleCloseTaskDetails = () => {
    //     setSelectedTask(null);
    //     setError(null);
    // };

    // const handleToggleComplete = async (id: number) => {
    //     try {
    //         const taskToUpdate = tasks.find(task => task.id === id);
    //         if (taskToUpdate) {
    //             const updatedTask = await jcefBridge.updateTodo({
    //                 ...taskToUpdate,
    //                 completed: !taskToUpdate.completed
    //             });
    //             setTasks(tasks.map(task => task.id === id ? updatedTask : task));
    //             if (selectedTask && selectedTask.id === id) {
    //                 setSelectedTask(updatedTask);
    //             }
    //         }
    //     } catch (err) {
    //         console.error('Failed to update task:', err);
    //         setError('Failed to update task. Please try again.');
    //     }
    // };

    if (loading) {
        return <div className="w-full h-screen flex items-center justify-center">Loading tasks...</div>;
    }

    if (error) {
        return <div className="w-full h-screen flex items-center justify-center text-red-500">{error}</div>;
    }

    return (
        <div className="w-full h-screen bg-gray-100 p-8">
            {/* Rest of the component remains the same */}
            <div className="w-full h-full bg-white rounded-lg shadow-lg relative">
                <div className="absolute top-1/2 left-0 right-0 border-t-2 border-gray-400"></div>
                <div className="absolute top-0 bottom-0 left-1/2 border-l-2 border-gray-400"></div>
                <div className="absolute top-0 left-0 w-1/2 h-1/2 bg-red-100"></div>
                <div className="absolute top-0 right-0 w-1/2 h-1/2 bg-yellow-100"></div>
                <div className="absolute bottom-0 left-0 w-1/2 h-1/2 bg-blue-100"></div>
                <div className="absolute bottom-0 right-0 w-1/2 h-1/2 bg-green-100"></div>

                <div className="absolute top-0 bottom-0 left-0 right-0">
                    <span className="absolute left-4 top-1/2 bg-white px-2 transform -translate-y-1/2">重要</span>
                    <span className="absolute right-4 top-1/2 bg-white px-2 transform -translate-y-1/2">不重要</span>
                    <span className="absolute top-4 left-1/2 bg-white px-2 transform -translate-x-1/2">紧急</span>
                    <span className="absolute bottom-4 left-1/2 bg-white px-2 transform -translate-x-1/2">不紧急</span>
                </div>

                {tasks.map((task) => (
                    <div
                        key={task.id}
                        style={getTaskPosition(task)}
                        className=" rounded-full text-black flex items-center justify-center cursor-pointer"
                        onClick={() => setSelectedTask(task)}
                    >
                        {task.id}
                    </div>
                ))}

                {selectedTask && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                        <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4 relative">
                            <button
                                onClick={() => setSelectedTask(null)}
                                className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
                            >
                                <X size={24}/>
                            </button>

                            <h2 className="text-xl font-bold mb-4">{selectedTask.title}</h2>

                            <div className="space-y-3">
                                <div className="flex items-center gap-2 text-gray-700">
                                    <CheckCircle2 size={20}/>
                                    <span>Status: {selectedTask.completed ? "Completed" : "Pending"}</span>
                                </div>

                                <div className="flex items-center gap-2 text-gray-700">
                                    <CalendarDays size={20}/>
                                    <span>Due Date: {selectedTask.dueDate}</span>
                                </div>

                                <div className="flex items-center gap-2 text-gray-700">
                                    <AlertCircle size={20}/>
                                    <span>Important: {selectedTask.importance}%</span>
                                </div>

                                <div className="flex items-center gap-2 text-gray-700">
                                    <ListTodo size={20}/>
                                    <span>List: {selectedTask.list}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}