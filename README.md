# Social Media Platform (Full-stack Network Application)
**A high-performance social networking system emphasizing real-time communication and secure API architecture.**
---
##  Project Overview
Dự án là một nền tảng mạng xã hội hoàn chỉnh được xây dựng để giải quyết các bài toán cốt lõi trong **Lập trình mạng**: Truyền tải dữ liệu thời gian thực, quản lý phiên làm việc bảo mật và tối ưu hóa giao tiếp giữa Client-Server. Hệ thống hỗ trợ đầy đủ các tính năng tương tác như nhắn tin tức thời, quản lý bài đăng và thiết lập mạng lưới bạn bè.

##  Key Technical Highlights (Network Programming)
* **Real-time Messaging (WebSocket & STOMP):** Triển khai giao thức WebSocket để xử lý luồng dữ liệu hai chiều (Full-duplex). Tin nhắn và thông báo được đẩy (Push) trực tiếp từ Server tới Client mà không cần cơ chế Polling, giúp giảm thiểu độ trễ mạng.
* **RESTful API Architecture:** Thiết kế hệ thống Endpoint chuẩn hóa, xử lý các yêu cầu HTTP/HTTPS phức tạp, đảm bảo tính mở rộng và dễ bảo trì.
* **Security & Encryption:** Tích hợp **Spring Security** để bảo vệ các tài nguyên mạng, ngăn chặn các cuộc tấn công phổ biến và quản lý quyền truy cập của người dùng.
* **Asynchronous Communication:** Sử dụng các kỹ thuật xử lý bất đồng bộ ở cả Backend (Java) và Frontend (JavaScript `fetch/async-await`) để tránh tình trạng nghẽn luồng (Blocking) khi thực hiện các tác vụ mạng nặng.

## Tech Stack
### **Backend (The Core)**
* **Framework:** Spring Boot 3.x (Java 17+)
* **Networking:** WebSocket, STOMP Protocol
* **Security:** Spring Security & JWT Implementation
* **Build Tool:** Maven
* **Persistence:** Spring Data JPA (Hibernate)

### **Frontend**
* **Core:** Modern JavaScript (ES6+), HTML5, CSS3
* **Communication:** WebSocket Client, Fetch API
* **Pattern:** Modular JavaScript (Separation of Concerns)

##  System Architecture
Dự án tuân thủ nghiêm ngặt mô hình **Client-Server Separation**, giúp hệ thống linh hoạt và chuyên nghiệp:
* **Client Tier:** Các module JS được chia theo chức năng (`api.auth.js`, `api.chat.js`, `websocket.friends.js`) để quản lý logic giao tiếp mạng riêng biệt.
* **Server Tier:** Áp dụng kiến trúc **Layered Architecture**:
    * `Controller`: Điều phối các Request từ mạng.
    * `Service`: Thực thi logic nghiệp vụ và xử lý dữ liệu.
    * `Repository`: Tương tác tầng dữ liệu (Database).
    * `WebSocket Config`: Cấu hình Message Broker để điều phối tin nhắn Real-time.

##  Key Project Structure
```text
├── backend/
│   ├── src/main/java/com/socialmedia/
│   │   ├── config/      # System & Network configurations
│   │   ├── controller/  # REST API Gateways
│   │   ├── websocket/   # Real-time communication logic
│   │   └── security/    # User protection & Auth logic
├── frontend/
│   ├── js/
│   │   ├── api/         # Network request modules (Fetch/Axios)
│   │   └── pages/       # UI interaction logic
│   └── *.html           # Structural view templates
