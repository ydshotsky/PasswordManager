import Dashboard from "./Dashboard.jsx";
import {createBrowserRouter,RouterProvider} from "react-router-dom";

export default function Routes(){



    const routes=createBrowserRouter([
        {
            path:"/",
            element:<Dashboard/>
        }
    ]);

    return <RouterProvider router={routes} />



}