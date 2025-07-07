import { HashLoader } from "react-spinners";

export default function HashLoading() {
  return (
    <div
      style={{
        position: "fixed",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
        backgroundColor: "rgba(255, 255, 255, 0.5)",
        zIndex: 9999,
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backdropFilter: "blur(0.5px)",
      }}
    >
      <HashLoader
        color="#052C65"
        size={75}
        className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"
      />
    </div>
  );
}
