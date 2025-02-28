import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  products: any[] = [];
  errorMessage: string = '';
  isAdmin: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.loadProducts();
    this.checkAdminStatus();
  }

  loadProducts() {
    this.authService.getProducts().subscribe(
      response => {
        this.products = response;
        this.products.forEach(product => product.quantity = 0); // Default quantity
      },
      error => {
        this.errorMessage = 'Error loading products. Please log in.';
      }
    );
  }

  checkAdminStatus() {
    const userRole = localStorage.getItem('role'); // Assuming role is stored in localStorage
    this.isAdmin = userRole === 'ADMIN';
  }

  placeOrder() {
    const orderItems = this.products
      .filter(product => product.quantity && product.quantity > 0) // Only include selected products
      .map(product => ({
        productId: product.id,
        quantity: product.quantity
      }));

    if (orderItems.length === 0) {
      alert('Please select at least one product to order.');
      return;
    }

    this.authService.placeOrder(orderItems).subscribe({
      next: response => {
        alert(`Order placed successfully! Status: ${response.orderStatus}`);
        this.products.forEach(product => product.quantity = 0); // Reset quantities after order
      },
      error: err => {
        const errorMessage = err.error || 'Failed to place order. Please try again.';
        alert(errorMessage);
      }
    });
  }

  addToWatchlist(productId: number): void {
    this.authService.addToWatchlist(productId).subscribe({
      next: () => {
        alert('Product added to watchlist!');
      },
      error: err => {
        const errorMessage = err.error || 'Failed to add product to watchlist.'; // Use backend response if available
        alert(errorMessage);
      }
    });
  }

  navigateToAddProduct(): void {
    this.router.navigate(['/add-product']); // Ensure you have this route configured
  }
}

