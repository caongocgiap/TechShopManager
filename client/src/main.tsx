import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "@/assets/styles/index.css";
import App from "./App.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { Provider } from "react-redux";
import { StompSessionProvider } from "react-stomp-hooks";
import { URL_WEBSOCKET } from "./constants/index.constant.ts";
import store from "./context/redux/store.ts";
import reportWebVitals from "./reportWebVitals.ts";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 0,
      refetchOnWindowFocus: false,
      refetchOnMount: true,
      refetchOnReconnect: false,
      retryOnMount: false,
      staleTime: 10 * 1000,
      // cacheTime: 5 * 60 * 1000,
      // onError: (e) => console.error(e),
    },
    mutations: {
      retry: 0,
    },
  },
});

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <Provider store={store}>
      <QueryClientProvider client={queryClient}>
        <StompSessionProvider url={URL_WEBSOCKET}>
          <App />
        </StompSessionProvider>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </Provider>
  </StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
