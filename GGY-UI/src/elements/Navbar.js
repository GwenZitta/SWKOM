import React from 'react';
import { Link } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';

const Navbar = () => {
    return (
        <AppBar position="static">
            <Toolbar>
                <Link to="/" style={{ textDecoration: 'none', color: 'inherit', flexGrow: 1 }}>
                    <Typography variant="h6">
                        GGY
                    </Typography>
                </Link>
                <Button color="inherit" component={Link} to="/upload">
                    Upload
                </Button>
                <Button color="inherit" component={Link} to="/search">
                    Search
                </Button>
                <Button color="inherit" component={Link} to="/manage">
                    Manage
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;