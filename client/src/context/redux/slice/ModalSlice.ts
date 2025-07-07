import { createSlice } from "@reduxjs/toolkit";

const ModalSlice = createSlice({
  name: "modal",
  initialState: {
    modal: {
      isOpen: false,
    },
    modalPersonalInfo: {
      isOpen: false,
    },
  },
  reducers: {
    openModal: (state) => {
      state.modal.isOpen = true;
    },
    closeModal: (state) => {
      state.modal.isOpen = false;
    },
    openOrCloseModal: (state) => {
      state.modal.isOpen = !state.modal.isOpen;
    },
    openModalPersonalInfo: (state) => {
      state.modalPersonalInfo.isOpen = true;
    },
    closeModalPersonalInfo: (state) => {
      state.modalPersonalInfo.isOpen = false;
    },
    openOrCloseModalPersonalInfo: (state) => {
      state.modalPersonalInfo.isOpen = !state.modalPersonalInfo.isOpen;
    },
  },
});

export const {
  openModal,
  closeModal,
  openOrCloseModal,
  openModalPersonalInfo,
  closeModalPersonalInfo,
  openOrCloseModalPersonalInfo,
} = ModalSlice.actions;

export default ModalSlice.reducer;
