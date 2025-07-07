/* eslint-disable @typescript-eslint/no-explicit-any */
import { AxiosPromise } from "axios";
import { instanceNoAuth } from "@/base/request";

const URL_SUPPORT = "/support/mail";

export const sendSupportMail = ({
  subject,
  body,
  file,
  moduleAddress,
}: {
  subject: string;
  body: string;
  file?: any;
  moduleAddress: string;
}) => {
  const formData = new FormData();
  formData.append("subject", subject);
  formData.append("body", body);
  if (file) formData.append("file", file.originFileObj as File);
  formData.append("moduleAddress", moduleAddress);
  return instanceNoAuth({
    url: `${URL_SUPPORT}/send`,
    method: "POST",
    data: formData,
  }) as AxiosPromise;
};
