import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {NextUIProvider} from "@nextui-org/react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from "./App.jsx";
import {TrollInspectorBar} from "./main/TrollInspectorBar.jsx";
import BackGroundImage from "./main/BackGroundImage.jsx";
import {SummonerSearchBar} from "./main/SummonerSearchBar.jsx";

const router = createBrowserRouter([
    {
        path: '/',
        element: <App main={<BackGroundImage search={<SummonerSearchBar/>}/>} />,
    },
    {
        path: 'troll-inspector',
        element: <App main={<BackGroundImage search={<TrollInspectorBar/>}/>} />
    }
])

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
      <NextUIProvider>
          <RouterProvider router={router} />
      </NextUIProvider>
  </React.StrictMode>,
)
