import React, {useEffect, useState} from 'react';
import {ArrowLeft, Calendar, List, Search} from 'lucide-react';
import * as jcefBridge from './jcefBridge';

interface SearchResult {
    id: number;
    title: string;
    list: string;
}

interface SearchResultsPageProps {
    searchQuery: string;
    onBack: () => void;
}

const SearchResultsPage: React.FC<SearchResultsPageProps> = ({searchQuery, onBack}) => {
    const [searchResults, setSearchResults] = useState<SearchResult[]>([]);

    useEffect(() => {
        const fetchSearchResults = async () => {
            if (searchQuery.trim()) {
                try {
                    const results = await jcefBridge.searchTodos(searchQuery);
                    setSearchResults(results);
                } catch (err) {
                    console.error('Failed to search todos:', err);
                }
            } else {
                setSearchResults([]);
            }
        };

        fetchSearchResults();
    }, [searchQuery]);

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 头部区域 */}
            <div className="sticky top-0 bg-white shadow-sm z-10">
                <div className="max-w-3xl mx-auto px-4 py-3">
                    <div className="flex items-center gap-3">
                        <button
                            onClick={onBack}
                            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
                        >
                            <ArrowLeft className="w-5 h-5 text-gray-600"/>
                        </button>
                        {/* 可选地显示搜索查询 */}
                        <h1 className="text-xl font-semibold">{searchQuery}</h1>
                    </div>
                </div>
            </div>

            {/* 结果区域 */}
            <div className="max-w-3xl mx-auto px-4 py-6">
                {searchResults.length > 0 && (
                    <div className="text-sm text-gray-500 mb-4">找到 {searchResults.length} 个结果</div>
                )}

                <div className="space-y-4">
                    {searchResults.map((result) => (
                        <div
                            key={result.id}
                            className="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-lg transition-shadow duration-200 cursor-pointer"
                        >
                            <div className="flex items-start justify-between">
                                <div className="flex-grow">
                                    <h3 className="text-lg font-medium text-gray-900 mb-2">{result.title}</h3>
                                    <div className="flex items-center gap-4 text-sm text-gray-500">
                                        <div className="flex items-center gap-1">
                                            <List className="w-4 h-4"/>
                                            <span>{result.list}</span>
                                        </div>
                                        <div className="flex items-center gap-1">
                                            <Calendar className="w-4 h-4"/>
                                            <span>Today</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {searchResults.length === 0 && searchQuery.trim() && (
                    <div className="text-center py-12">
                        <div
                            className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gray-100 mb-4">
                            <Search className="w-8 h-8 text-gray-400"/>
                        </div>
                        <h3 className="text-lg font-medium text-gray-900 mb-1">未找到结果</h3>
                        <p className="text-gray-500">试试使用其他关键词搜索</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default SearchResultsPage;