import React, { useState } from 'react';
import {Card, CardContent, List, ListItem, TextField, Typography} from "@mui/material";
import '../App.css'

const Search = ({ uploadedFiles }) => {
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    // Filter files based on search query
    const filteredFiles = uploadedFiles.filter(fileName =>
        fileName.toLowerCase().includes(searchQuery.toLowerCase())
    );

    return (
        <div className={"pageContent"}>
            <h1>Search Uploaded Files</h1>
            <TextField
                label="Search files..."
                variant="outlined"
                fullWidth
                value={searchQuery}
                onChange={handleSearchChange}
            />
            <h3>Uploaded Files:</h3>
            <ul>
                {filteredFiles.length > 0 ? (
                    filteredFiles.map((fileName, index) => (
                        <li key={index}>{fileName}</li>
                    ))
                ) : (
                    <li>No files found</li>
                )}
            </ul>

            <p style={{ color: 'red' }}>Es gibt zur Zeit einen Fehler bei der Suche, wir sind dabei ihn zu reparieren! Die Dokumente werden dennoch in Elasticsearch gespeichert, auf localhost:5601 (Kibana-UI) kann dies überprüft werden!</p>

        </div>
    );
};

export default Search;
