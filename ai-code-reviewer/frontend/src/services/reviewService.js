import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

// STAGE 4: call this from App.js when the user clicks "Review"
export async function reviewCode(code, language, action) {
  const response = await axios.post(`${API_BASE_URL}/review`, {
    code,
    language,
    action,
  });
  return response.data;
}
