import './App.css';
import React, {useState} from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./pages/Layout";
import Home from "./pages/Home";
import Upload from "./pages/Upload";
import Search from "./pages/Search";
import Manage from "./pages/Manage";
import Error from "./pages/Error";

export default function App() {
    const [uploadedFiles, setUploadedFiles] = useState([]);

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path="upload" element={<Upload setUploadedFiles={setUploadedFiles} />} />
                    <Route path="search" element={<Search uploadedFiles={uploadedFiles} />} />
                    <Route path="manage" element={<Manage />} />
                    <Route path="*" element={<Error />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}
