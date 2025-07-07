import { memo } from "react";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default memo(function Footer({ style }: { style?: any }) {
  return (
    <footer
      style={{
        ...style,
        bottom: 0,
        width: "100%",
        height: "auto",
        display: "flex",
        flexDirection: "row",
        justifyContent: "center",
        alignItems: "center",
        flexWrap: "wrap",
        zIndex: 1000,
        padding: "10px 0",
        fontSize: "1.3rem",
        color: "#172b4d !important",
      }}
    >
      <div className="text-muted-foreground">
        Powered by 💖 <strong>@Ngcoo.Giapw_</strong>
      </div>
    </footer>
  );
});
