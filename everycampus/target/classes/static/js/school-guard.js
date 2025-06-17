function guardSchool(expected) {
  const school = localStorage.getItem("school") || "";
  if (school.trim() !== expected) {
    localStorage.removeItem("username");
    localStorage.removeItem("school");

    alert(`이 페이지는 ${expected} 사용자만 접근할 수 있습니다.\n로그인 화면으로 이동합니다.`);
    location.href = "/login.html";
  }
}
