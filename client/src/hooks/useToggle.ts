import { Dispatch, SetStateAction, useCallback, useState } from "react";

function useToggle(defaultValue?: boolean): {
  value: boolean;
  toggle: () => void;
  setValue: Dispatch<SetStateAction<boolean>>;
  setTrue: () => void;
  setFalse: () => void;
} {
  const [value, setValue] = useState(!!defaultValue);

  const toggle = useCallback(() => setValue((x) => !x), []);

  const setTrue = useCallback(() => setValue(true), []);

  const setFalse = useCallback(() => setValue(false), []);

  return { value, toggle, setValue, setTrue, setFalse };
}

export default useToggle;
