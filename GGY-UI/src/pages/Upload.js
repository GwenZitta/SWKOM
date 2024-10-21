import React, {useState} from 'react';
import {Button, Card, CardContent, Typography} from "@mui/material";
import '../App.css'

function Upload({ setUploadedFiles }) {
    const [file, setFile] = useState(null);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file) {
            alert("Please select a file to upload.");
            return;
        }

        const formData = new FormData();
        formData.append('file', file); // Append the file to FormData

        const uploadUrl = 'http://localhost:8081/upload';

        try {
            const response = await fetch(uploadUrl, {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                alert("File uploaded successfully!");
                setUploadedFiles(prevFiles => [...prevFiles, file.name]);
                setFile(null);
            } else {
                alert("File upload failed.");
            }
        } catch (error) {
            console.error("Error uploading file:", error);
            alert("Error uploading file.");
        }
    };
    return (
        <div className={"pageContent"}>
            <h1>Upload Page</h1>
            <h3>Here you can upload your files!</h3>
            <input type="file" onChange={handleFileChange} />
            <Button variant="contained" color="primary" onClick={handleUpload}>
                Upload
            </Button>
        </div>
    );
}

export default Upload;