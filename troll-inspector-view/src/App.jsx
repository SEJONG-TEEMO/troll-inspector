import {Header} from "./header/Header.jsx";
import React from "react";
import Bottom from "./footer/Bottom.jsx"

function App(props){

  return (
    <>
        <Header/>
            {props.main}
        <Bottom />
    </>
  )
}

export default App
