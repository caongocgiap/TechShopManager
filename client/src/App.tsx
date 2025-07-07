import { BrowserRouter } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { Fragment } from "react/jsx-runtime";
import RouteList from "./route/RouteList";
import { ThemeProvider } from "@/components/theme-provider";

function App() {
  return (
    <ThemeProvider>
      <Fragment>
        <BrowserRouter>
          <RouteList />
        </BrowserRouter>
        <ToastContainer />
      </Fragment>
    </ThemeProvider>
  );
}

export default App;
