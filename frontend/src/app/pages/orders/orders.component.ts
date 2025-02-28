import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-orders',
  standalone: false,
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  isAdmin: boolean = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.loadOrders();
    this.checkAdminStatus();
  }

  loadOrders() {
    this.authService.getOrders().subscribe(
      response => {
        this.orders = response;
      },
      error => {
        this.errorMessage = 'Error loading orders. Please try again.';
      }
    );
  }

  checkAdminStatus() {
    this.isAdmin = localStorage.getItem('role') === 'ADMIN';
  }

  viewOrderDetails(orderId: number) {
    this.router.navigate(['/order', orderId]); // Navigates to the order detail page
  }
}
