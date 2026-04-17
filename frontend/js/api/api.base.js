(function (global) {
  // api.base.js
  // Helper để kết nối với backend, quản lý token, và các request GET/POST/PUT/DELETE.
  // Sử dụng dưới dạng global `api` (ví dụ: api.get('/api/posts')).

  const DEFAULT_BASE = window.BACKEND_URL || 'http://localhost:8080';

  function buildQuery(params) {
    if (!params || Object.keys(params).length === 0) return '';
    return '?' + Object.keys(params)
      .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
      .join('&');
  }

  const Api = {
    baseUrl: DEFAULT_BASE,

    setBaseUrl(url) {
      this.baseUrl = url;
    },

    getToken() {
      return localStorage.getItem('token');
    },

    setToken(token) {
      localStorage.setItem('token', token);
    },

    clearToken() {
      localStorage.removeItem('token');
    },

    getAuthHeaders() {
      const t = this.getToken();
      return t ? { 'Authorization': 'Bearer ' + t } : {};
    },

    buildUrl(path, params) {
      // allow passing full URL
      if (/^https?:\/\//i.test(path)) {
        return path + buildQuery(params);
      }
      // ensure slash
      const base = this.baseUrl.replace(/\/$/, '');
      const p = path.startsWith('/') ? path : '/' + path;
      return base + p + buildQuery(params);
    },

    async request(path, opts = {}) {
      const {
        method = 'GET',
        data = null,
        headers = {},
        params = {},
        parseJson = true,
        redirectOn401 = true,
      } = opts;

      const url = this.buildUrl(path, params);
      
      // Debug log
      if (typeof debugLog !== 'undefined') {
        debugLog(`🌐 API ${method}:`, path);
      }

      const authHeaders = this.getAuthHeaders();
      const init = {
        method,
        headers: Object.assign({}, headers, authHeaders),
      };

      // handle body
      if (data != null) {
        if (data instanceof FormData) {
          // Let browser set Content-Type including boundary
          init.body = data;
        } else if (typeof data === 'object') {
          init.headers['Content-Type'] = 'application/json';
          init.body = JSON.stringify(data);
          // Debug: log request body
          console.log(`📤 Request Body for ${method} ${path}:`, data);
        } else {
          init.body = data;
        }
      }

      const res = await fetch(url, init);

      if (!res.ok) {
        // optional redirect on unauthorized
        if (res.status === 401 && redirectOn401) {
          // DEBUG: log this to localStorage before redirect
          if (typeof debugLog !== 'undefined') {
            debugLog('❌ API 401 UNAUTHORIZED detected!', {path: path, method: method, url: url});
          }
          // clear token and redirect to login page
          this.clearToken();
          // best-effort redirect - pages may override
          window.location.href = 'login.html';
          return Promise.reject({ status: res.status, message: 'Unauthorized' });
        }

        // try to parse error body
        let errBody = null;
        try {
          errBody = await res.json();
        } catch (e) {
          errBody = await res.text().catch(() => null);
        }

        // Debug: log error response
        console.error(`❌ API Error ${res.status} ${res.statusText}:`, {path, method, url, errBody});

        const err = new Error('Request failed: ' + res.status + ' ' + res.statusText);
        err.status = res.status;
        err.body = errBody;
        throw err;
      }

      if (!parseJson) return res;

      // Parse JSON, but handle empty body
      const text = await res.text();
      if (!text) return null;
      try {
        return JSON.parse(text);
      } catch (e) {
        // Not JSON: return raw text
        return text;
      }
    },

    get(path, params, opts = {}) {
      return this.request(path, Object.assign({}, opts, { method: 'GET', params }));
    },

    post(path, data, opts = {}) {
      return this.request(path, Object.assign({}, opts, { method: 'POST', data }));
    },

    put(path, data, opts = {}) {
      return this.request(path, Object.assign({}, opts, { method: 'PUT', data }));
    },

    del(path, data, opts = {}) {
      return this.request(path, Object.assign({}, opts, { method: 'DELETE', data }));
    },

    // Helper to wrap auth-required calls and return only .data when backend uses { data: ... }
    async authFetch(path, opts = {}) {
      return this.request(path, Object.assign({}, opts, { redirectOn401: true }));
    },

    // Get avatar URL with fallback to default
    getAvatarUrl(avatarUrl) {
      return avatarUrl && avatarUrl.trim() !== '' ? avatarUrl : 'images/avatars/default_avatar.png';
    }
  };

  // Expose on global
  global.api = Api;

})(window);
