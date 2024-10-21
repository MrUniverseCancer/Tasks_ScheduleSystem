import React, {useEffect, useRef, useState} from 'react';
import {ChevronRight, Search} from 'lucide-react';

interface SearchResult {
    id: number;
    title: string;
    list: string;
}

interface SearchBoxProps {
    onSearch: (query: string) => void;
    onViewMore: () => void;
}

const SearchBox: React.FC<SearchBoxProps> = ({onSearch, onViewMore}) => {
    const [isSearchFocused, setIsSearchFocused] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
    const searchRef = useRef<HTMLDivElement>(null);

    // Sample search results (replace with actual search logic later)
    const sampleSearchResults: SearchResult[] = [
        {id: 1, title: 'Add task to project', list: 'Work'},
        {id: 2, title: 'Buy groceries for a dinner', list: 'Personal'},
        {id: 3, title: 'Prepare presentation for meeting', list: 'Work'},
        {id: 4, title: 'Call mom', list: 'Personal'},
        {id: 5, title: 'Review quarterly report', list: 'Work'},
    ];

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (searchRef.current && !searchRef.current.contains(event.target as Node)) {
                setIsSearchFocused(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleSearchFocus = () => {
        setIsSearchFocused(true);
    };

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const query = e.target.value;
        setSearchQuery(query);

        // Filter sample results based on query (replace with actual search logic later)
        const filteredResults = sampleSearchResults.filter(result =>
            result.title.toLowerCase().includes(query.toLowerCase())
        );
        setSearchResults(filteredResults);
    };

    const handleSearchSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSearch(searchQuery);
    };

    return (
        <div
            ref={searchRef}
            className={`relative flex items-center bg-white rounded-md transition-all duration-300 ease-in-out ${isSearchFocused ? 'w-96' : 'w-64'
            }`}
        >
            <form onSubmit={handleSearchSubmit} className="flex items-center w-full">
                <Search className="text-gray-400 ml-2"/>
                <input
                    type="text"
                    placeholder="搜索"
                    className="p-2 rounded-md text-black focus:outline-none w-full"
                    onFocus={handleSearchFocus}
                    value={searchQuery}
                    onChange={handleSearchChange}
                />
            </form>
            {isSearchFocused && searchQuery && (
                <div className="absolute top-full left-0 w-full bg-white mt-1 rounded-md shadow-lg z-10">
                    {searchResults.slice(0, 4).map((result) => (
                        <div key={result.id} className="flex items-center justify-between p-2 hover:bg-gray-100">
                            <span className="truncate flex-grow mr-2 text-black">{result.title}</span>
                            <span className="text-gray-500 text-sm truncate w-20 text-right">{result.list}</span>
                        </div>
                    ))}
                    {searchResults.length > 4 && (
                        <div
                            className="p-2 text-blue-600 hover:bg-gray-100 cursor-pointer flex items-center justify-between"
                            onClick={onViewMore}
                        >
                            <span>查看更多</span>
                            <ChevronRight size={16}/>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchBox;