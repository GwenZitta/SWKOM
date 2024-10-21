import { Outlet, Link } from "react-router-dom";
import Navbar from '../elements/Navbar'

const Layout = () => {
    return (
        <div>
            <Navbar />
            <Outlet />
        </div>
    );
};

export default Layout;
