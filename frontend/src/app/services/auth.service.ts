import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, BehaviorSubject, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  // BehaviorSubjects to track authentication state
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private isAdminSubject = new BehaviorSubject<boolean>(this.checkAdmin());
  isAdmin$ = this.isAdminSubject.asObservable();

  constructor(private http: HttpClient) {}

  register(userData: { username: string; email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/signup`, userData);
  }

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.storeUserData(response)) // Store data in localStorage
    );
  }

  private storeUserData(response: any): void {
    localStorage.setItem('token', response.token); // Store JWT token
    localStorage.setItem('role', response.role);   // Store user role

    // Update authentication state
    this.isAuthenticatedSubject.next(true);
    this.isAdminSubject.next(this.checkAdmin());
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('token'); // Returns true if a token exists
  }

  private checkAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');

    this.isAuthenticatedSubject.next(false);
    this.isAdminSubject.next(false);
  }

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getProducts(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/products/all`, { headers: this.getAuthHeaders() });
  }

  getProductById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/products/${id}`, { headers: this.getAuthHeaders() });
  }

  placeOrder(orderItems: { productId: number; quantity: number }[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/orders`, { order: orderItems }, { headers: this.getAuthHeaders() });
  }

  getOrders(): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders/all`, { headers: this.getAuthHeaders() });
  }

  getOrderById(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders/${orderId}`, { headers: this.getAuthHeaders() });
  }

  cancelOrder(orderId: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/orders/${orderId}/cancel`, {}, { headers: this.getAuthHeaders() });
  }

  completeOrder(orderId: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/orders/${orderId}/complete`, {}, { headers: this.getAuthHeaders() });
  }

  addToWatchlist(productId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/watchlist/product/${productId}`, {}, {
      headers: this.getAuthHeaders(),
      responseType: 'text' as 'json'
    });
  }

  removeFromWatchlist(productId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/watchlist/product/${productId}`, {
      headers: this.getAuthHeaders(),
      responseType: 'text' as 'json'
    });
  }

  getWatchlistProducts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/watchlist/products/all`, { headers: this.getAuthHeaders() });
  }

  getFrequentPurchases(): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/frequent/3`, { headers: this.getAuthHeaders() });
  }

  getRecentPurchases(): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/recent/3`, { headers: this.getAuthHeaders() });
  }

  getMostProfitable(): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/profit/3`, { headers: this.getAuthHeaders() });
  }

  getMostPopular(): Observable<any> {
    return this.http.get(`${this.apiUrl}/products/popular/3`, { headers: this.getAuthHeaders() });
  }


  addProduct(productData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/products`, productData, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  updateProduct(productId: number, updatedProduct: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/products/${productId}`, updatedProduct, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

}
