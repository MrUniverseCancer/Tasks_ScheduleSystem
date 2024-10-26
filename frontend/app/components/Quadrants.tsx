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
        const size = 40 + ((urgency > 50) ? (urgency-50) * 0.3 : 0); // 根据紧迫度设置大小，范围从30到45
        const color = `hsl(${(1 - task.importance / 100) * 120}, 100%, 50%)`; // 绿色到红色的渐变

        // 由于圆心对齐边界会超出，做0.95收缩处理
        const processInteger = (value: number): number => {
            // 数学处理：平方输入值
            return (value - 50) * 0.95 + 50;
        };

        return {
            position: "absolute" as const,
            right: `${processInteger(urgency)}%`,
            bottom: `${processInteger(task.importance)}%`,
            transform: "translate(50%, 50%)",
            width:  `${size}px`,
            height: `${size}px`,
            backgroundColor: color,
            border: "1px solid black", // 添加黑色分界线
            borderRadius: "50%" // 确保是圆形
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
                <div className="absolute top-0 left-0 right-4 border-t-4 border-gray-800"></div>
                <div className="absolute top-0 bottom-0 left-0 border-l-4 border-gray-800"></div>
                <div className="absolute top-1 left-1 w-1/2 h-1/2 bg-red-100"></div>
                <div className="absolute top-1 right-0 w-1/2 h-1/2 bg-yellow-100"></div>
                <div className="absolute bottom-0 left-1 w-1/2 h-1/2 bg-blue-100"></div>
                <div className="absolute bottom-0 right-0 w-1/2 h-1/2 bg-green-100"></div>
                {/* 箭头 */}
                <div className="absolute -top-2 right-2 w-0 h-0 border-l-8 border-l-transparent border-b-8 border-b-gray-800 transform -rotate-45" style={{ marginTop: '5px', marginRight: '8px' }}></div>
                <div className="absolute -bottom-0 -left-0.5 w-0 h-0 border-l-8 border-l-transparent border-b-8 border-b-gray-800 transform rotate-45" style={{ marginTop: '5px', marginRight: 'px' }}></div>

                <div className="absolute top-0 bottom-0 left-0 right-0">
                    <span className="absolute -left-5 -bottom-10  px-2 transform -translate-y-1/2">重要性</span>
                    <span className="absolute -top-6 -right-8  px-2 transform -translate-x-1/2">紧急性</span>
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