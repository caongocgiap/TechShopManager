import { useTheme } from "@/components/theme-provider";
import { Link } from "react-router-dom";
import ComputerSearch from "../components/filter/SearchBar";
import { Sun, Moon } from "lucide-react";
import Logo_FPT from "@/assets/images/Logo_FPT.png";
import Logo_FPoly from "@/assets/images/Logo_FPoly.png";
import { UserAvatar } from "@/components/userinformation/user-avatar";

const Header = () => {
  const { theme, setTheme } = useTheme();
  const isDarkMode = theme === "dark";

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <Link
          to="/"
          className="text-2xl font-bold text-gray-900 dark:text-white"
        >
          <img
            src={theme === "dark" ? Logo_FPoly : Logo_FPT}
            alt="logo"
            className="h-14"
          />
        </Link>

        <div className="flex gap-4">
          <ComputerSearch />
          <div
            onClick={() => setTheme(isDarkMode ? "light" : "dark")}
            className={`flex items-center cursor-pointer transition-transform duration-500 ${
              isDarkMode ? "rotate-180" : "rotate-0"
            }`}
          >
            {isDarkMode ? (
              <Sun className="h-6 w-6 text-yellow-500 rotate-0 transition-all" />
            ) : (
              <Moon className="h-6 w-6 text-blue-500 rotate-0 transition-all" />
            )}
          </div>
          <UserAvatar />
        </div>
      </div>
    </header>
  );
};

export default Header;
