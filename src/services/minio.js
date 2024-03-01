import {request} from "umi";

export async function uploadFile(url, file) {
  const formData = new FormData()
  formData.append("file", file)
  return request(`/minio/${url}`, {
    method: 'POST',
    body: formData
  });
}

