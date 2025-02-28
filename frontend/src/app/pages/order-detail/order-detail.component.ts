import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-order-detail',
  standalone: false,
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.css'
})
export class OrderDetailComponent implements OnInit {
  order: any;
  isAdmin: boolean = false;
  errorMessage: string = '';

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit() {
    this.checkAdminStatus();
    this.loadOrderDetails();
  }

  checkAdminStatus() {
    this.isAdmin = localStorage.getItem('role') === 'ADMIN';
  }

  loadOrderDetails() {
    const orderId = this.route.snapshot.paramMap.get('id');
    if (orderId) {
      this.authService.getOrderById(parseInt(orderId)).subscribe(
        response => {
          this.order = response;
        },
        error => {
          this.errorMessage = 'Error loading order details.';
        }
      );
    }
  }

  cancelOrder() {
    const orderId = this.route.snapshot.paramMap.get('id'); // Fetch ID from URL
    if (!orderId) {
      alert('Error: Order ID not found.');
      return;
    }

    this.authService.cancelOrder(parseInt(orderId)).subscribe(
      response => {
        alert('Order cancelled successfully!');
        this.loadOrderDetails(); // Refresh order details
      },
      error => {
        alert('Failed to cancel order.');
      }
    );
  }

  completeOrder() {
    const orderId = this.route.snapshot.paramMap.get('id'); // Fetch ID from URL
    if (!orderId) {
      alert('Error: Order ID not found.');
      return;
    }

    this.authService.completeOrder(parseInt(orderId)).subscribe(
      response => {
        alert('Order completed successfully!');
        this.loadOrderDetails(); // Refresh order details
      },
      error => {
        alert('Failed to complete order.');
      }
    );
  }

}
