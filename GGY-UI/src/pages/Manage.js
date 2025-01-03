import React, { useEffect, useState } from 'react';
import '../App.css';

function Manage() {
    // State für die Dokumentliste
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(true); // State für den Ladevorgang
    const [error, setError] = useState(null); // State für Fehler

    // useEffect zum Abrufen der Dokumente bei Komponentenerstellung
    useEffect(() => {
        fetch('/manage/all')
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP-Error: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                setDocuments(data); // Speichere die Dokumente im State
                setLoading(false); // Ladeindikator beenden
            })
            .catch((error) => {
                setError(error.message); // Fehler speichern
                setLoading(false); // Ladeindikator beenden
            });
    }, []);

    return (
        <div className={"pageContent"}>
            <h1>Manage Page</h1>
            <p>Here you can manage your files!</p>

            {/* Ladeindikator */}
            {loading && <p>Loading documents...</p>}

            {/* Fehleranzeige */}
            {error && <p style={{ color: 'red' }}>Sry es ist ein Fehler beim Abrufen der Dokumente aus der Datenbank aufgetreten</p>}
            {error && <p style={{ color: 'red' }}>Error: {error}</p>}

            {/* Dokumentliste */}
            {!loading && !error && (
                <div>
                    {documents.length === 0 ? (
                        <p>No documents found.</p> // Wenn keine Dokumente vorhanden sind
                    ) : (
                        <ul>
                            {documents.map((doc) => (
                                <li key={doc.id}>
                                    <h3>{doc.name}</h3>
                                    <p>Type: {doc.documentType}</p>
                                    <p>Date: {doc.datetime}</p>
                                    <a href={doc.pathToDocument} target="_blank" rel="noopener noreferrer">
                                        View Document
                                    </a>
                                    <hr />
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}
        </div>
    );
}

export default Manage;
