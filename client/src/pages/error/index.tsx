import logoFpoly from "@/assets/images/Logo_FPoly.png";
import logoUDPM from "@/assets/images/logo-udpm-dark.png";
import { memo, useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import "./index.css";

const Error = () => {
  const [searchParams] = useSearchParams();

  const [errorMess, setErrorMess] = useState<string | null>(null);

  useEffect(() => {
    const error = searchParams.get("error");
    if (error) setErrorMess(error);
  }, [searchParams]);

  return (
    <div className="page-container h-[100vh] flex flex-col justify-between">
      <div className="content-error">
        <div className="my-4 flex justify-center items-center">
          <img width="20%" src={logoFpoly} alt="Logo" />
          <img width="20%" src={logoUDPM} alt="Logo" />
        </div>
        <h2>Error</h2>
        <h3>{errorMess}</h3>
        <Link to="/" className="mt-4 bg-[#253741] border-[#253741] text-white">
          Đăng nhập lại
        </Link>
      </div>
    </div>
  );
};

export default memo(Error);
