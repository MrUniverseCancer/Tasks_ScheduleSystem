export interface Todo {
    id: number;
    title: string;
    completed: boolean;
    dueDate: string;
    importance: number;
    list: string;     // 用于前端显示
    list_id: number;  // 用于与后端交互
}

export interface List {
    id: number;
    name: string;
    icon: string;
    isDefault?: boolean;
}

declare global {
    interface Window {
        cefQuery: (params: {
            request: string;
            onSuccess: (response: string) => void;
            onFailure: (error_code: number, error_message: string) => void;
        }) => void;
    }
}

const callBridge = async <T>(action: string, params: Record<string, unknown> = {}): Promise<T> => {
    return new Promise((resolve, reject) => {
        window.cefQuery({
            request: JSON.stringify({action, ...params}),
            onSuccess: (response: string) => resolve(JSON.parse(response) as T),
            onFailure: (_error_code: number, error_message: string) => reject(new Error(error_message))
        });
    });
};

function toRecord<T extends object>(obj: T): Record<string, unknown> {
    return Object.entries(obj).reduce((acc, [key, value]) => {
        acc[key] = value;
        return acc;
    }, {} as Record<string, unknown>);
}

export const addTodo = async (todo: Omit<Todo, 'id'>): Promise<Todo> => {
    return callBridge<Todo>('addTodo', toRecord(todo));
};

export const deleteTodo = async (id: number): Promise<void> => {
    await callBridge<void>('deleteTodo', {id});
};

export const getAllTodos = async (): Promise<Todo[]> => {
    return callBridge<Todo[]>('getAllTodos');
};

export const updateTodo = async (todo: Todo): Promise<Todo> => {
    return callBridge<Todo>('updateTodo', toRecord(todo));
};

export const getTodo = async (id: number): Promise<Todo> => {
    return callBridge<Todo>('getTodo', {id});
};

export const sortTodos = async (criteria: string, direction: 'asc' | 'desc'): Promise<Todo[]> => {
    return callBridge<Todo[]>('sortTodos', {criteria, direction});
};

export const addList = async (list: Omit<List, 'id'>): Promise<List> => {
    return callBridge<List>('addList', toRecord(list));
};

export const getAllLists = async (): Promise<List[]> => {
    return callBridge<List[]>('getAllLists');
};

export const updateList = async (list: List): Promise<List> => {
    return callBridge<List>('updateList', toRecord(list));
};

export const deleteList = async (id: number): Promise<void> => {
    await callBridge<void>('deleteList', {id});
};

export const searchTodos = async (query: string): Promise<Todo[]> => {
    return callBridge<Todo[]>('searchTodos', {query});
};

