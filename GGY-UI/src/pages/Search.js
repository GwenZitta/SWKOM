import React, { useState } from 'react';
import { TextField, Typography, List, ListItem } from "@mui/material";
import '../App.css';

const Search = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const handleSearchChange = async (event) => {
        const query = event.target.value;
        setSearchQuery(query);

        // Only send request if the query is not empty
        if (query.trim() === '') {
            setSearchResults([]);
            return;
        }

        setIsLoading(true);
        try {
            const response = await fetch(`/api/search?query=${encodeURIComponent(query)}`);
            if (!response.ok) {
                throw new Error(`Error: ${response.statusText}`);
            }
            const data = await response.json();
            setSearchResults(data);
        } catch (error) {
            console.error('Failed to fetch search results:', error);
            setSearchResults([]);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="pageContent">
            <Typography variant="h4" gutterBottom>Search Uploaded Files</Typography>
            <TextField
                label="Search files..."
                variant="outlined"
                fullWidth
                value={searchQuery}
                onChange={handleSearchChange}
                autoFocus
            />
            <Typography variant="h6" gutterBottom>Search Results:</Typography>
            {isLoading ? (
                <Typography>Loading...</Typography>
            ) : (
                <List>
                    {searchResults.length > 0 ? (
                        searchResults.map((result, index) => (
                            <ListItem key={index}>
                                <Typography>{result.name}</Typography>
                            </ListItem>
                        ))
                    ) : (
                        <Typography>No results found</Typography>
                    )}
                </List>
            )}
        </div>
    );
};

export default Search;
