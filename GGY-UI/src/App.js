import './App.css';
import React, { useState } from 'react';


function App() {
    const [data, setData] = useState(null);


    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8081/');
    xhr.onload = function() {
        if (xhr.status === 200) {
            setData(xhr.responseText);
        }
    };
    xhr.send();

    return (
        <div>
            <p>
                {data}
            </p>
        </div>
    );
}

export default App;
