import React, { useState } from 'react';
import { Button, TextField } from "@mui/material";
import '../App.css';

function Upload({ setUploadedFiles }) {
    const [file, setFile] = useState(null);
    const [name, setName] = useState('');
    const [documenttype, setDocumentType] = useState('');
    const [datetime, setDatetime] = useState('');

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file || !name || !documenttype || !datetime) {
            alert("Please fill out all fields and select a file to upload.");
            return;
        }

        const formData = new FormData();
        formData.append('file', file); // Append the file to FormData
        formData.append('name', name);
        formData.append('documenttype', documenttype);
        formData.append('datetime', datetime);

        const uploadUrl = 'http://localhost:8081/document';

        try {
            const response = await fetch(uploadUrl, {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                alert("File and document uploaded successfully!");
                setUploadedFiles(prevFiles => [...prevFiles, name]);
                setFile(null);
                setName('');
                setDocumentType('');
                setDatetime('');
            } else {
                alert("Upload failed.");
            }
        } catch (error) {
            console.error("Error uploading file and document:", error);
            alert("Error uploading file and document.");
        }
    };

    return (
        <div className="pageContent">
            <h1>Upload Page</h1>
            <h3>Here you can upload your documents!</h3>

            <TextField
                label="Document Name"
                variant="outlined"
                fullWidth
                margin="normal"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <TextField
                label="Document Type"
                variant="outlined"
                fullWidth
                margin="normal"
                value={documenttype}
                onChange={(e) => setDocumentType(e.target.value)}
            />
            <TextField
                label="Date and Time"
                type="datetime-local"
                variant="outlined"
                fullWidth
                margin="normal"
                InputLabelProps={{
                    shrink: true,
                }}
                value={datetime}
                onChange={(e) => setDatetime(e.target.value)}
            />
            <input type="file" onChange={handleFileChange} style={{ marginTop: '20px' }} />

            <Button variant="contained" color="primary" onClick={handleUpload} style={{ marginTop: '20px' }}>
                Upload
            </Button>
        </div>
    );
}

export default Upload;
