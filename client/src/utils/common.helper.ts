/* eslint-disable @typescript-eslint/no-explicit-any */
export function sortObjectKeys(obj: Record<string, any>) {
  if (!obj) return obj;

  return sortAlphaText(Object.keys(obj)).reduce((acc, key) => {
    acc[key] = obj[key];

    return acc;
  }, {});
}

export function sortAlphaText(arr: string[], type?: "asc" | "desc") {
  if (!arr) return arr;

  return arr.sort((a, b) => {
    return a.localeCompare(b) * (type === "asc" ? 1 : -1);
  });
}
