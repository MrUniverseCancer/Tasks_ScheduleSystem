import React, {useEffect, useState} from 'react';
import {ArrowLeft, Search} from 'lucide-react';
import * as jcefBridge from './jcefBridge';

interface SearchResult {
    id: number;
    title: string;
    list: string;
}

interface SearchResultsPageProps {
    initialQuery: string;
    onBack: () => void;
}

const SearchResultsPage: React.FC<SearchResultsPageProps> = ({initialQuery, onBack}) => {
    const [searchQuery, setSearchQuery] = useState(initialQuery);
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


    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(e.target.value);
    };

    return (
        <div className="p-4">
            <div className="flex items-center mb-4">
                <button onClick={onBack} className="mr-4">
                    <ArrowLeft size={24}/>
                </button>
                <div className="relative flex-grow">
                    <input
                        type="text"
                        value={searchQuery}
                        onChange={handleSearchChange}
                        placeholder="搜索"
                        className="w-full p-2 pl-10 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"/>
                </div>
            </div>
            <div>
                {searchResults.map((result) => (
                    <div key={result.id} className="flex items-center justify-between p-2 hover:bg-gray-100 rounded-md">
                        <span className="truncate flex-grow mr-2">{result.title}</span>
                        <span className="text-gray-500 text-sm truncate w-20 text-right">{result.list}</span>
                    </div>
                ))}
                {searchResults.length === 0 && (
                    <p className="text-center text-gray-500 mt-4">No results found</p>
                )}
            </div>
        </div>
    );
};

export default SearchResultsPage;