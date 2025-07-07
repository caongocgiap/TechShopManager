import { ClipLoader } from "react-spinners";

const SpinnerLoading = () => {
  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        backgroundColor: "rgba(0, 0, 0, 0.5)",
        zIndex: 9999,
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backdropFilter: "blur(0.1px)",
      }}
    >
      <ClipLoader
        color="#052C65"
        size={80}
        speedMultiplier={1}
        aria-label="spinner-loading"
      />
    </div>
  );
};

export default SpinnerLoading;
