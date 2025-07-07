import { configureStore } from "@reduxjs/toolkit";
import AuthSlice from "./slice/AuthSlice";
import ModalSlice from "./slice/ModalSlice";

const store = configureStore({
  reducer: {
    auth: AuthSlice,
    modal: ModalSlice,
  },
  devTools: import.meta.env.VITE_APP_NODE_ENV !== "production",
});

export default store;

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;
