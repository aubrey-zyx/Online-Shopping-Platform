import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-product-detail',
  standalone: false,
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {
  product: any;
  productForm: FormGroup;
  isAdmin: boolean = false;
  message: string = '';
  isSuccess: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      retailPrice: [0, [Validators.required, Validators.min(0)]],
      wholesalePrice: [0, [Validators.required, Validators.min(0)]],
      quantity: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    this.isAdmin = localStorage.getItem('role') === 'ADMIN'; // Check if user is admin

    if (productId) {
      this.authService.getProductById(parseInt(productId)).subscribe({
        next: (response) => {
          this.product = response;

          // If the user is an admin, pre-fill the form with product data
          if (this.isAdmin) {
            this.productForm.patchValue(this.product);
          }
        },
        error: () => {
          this.message = 'Error loading product details.';
          this.isSuccess = false;
        }
      });
    }
  }

  updateProduct(): void {
    if (this.productForm.invalid) {
      this.message = 'Please fill all fields correctly.';
      this.isSuccess = false;
      return;
    }

    this.authService.updateProduct(this.product.id, this.productForm.value).subscribe({
      next: () => {
        this.message = 'Product updated successfully!';
        this.isSuccess = true;
      },
      error: (err) => {
        this.message = err.error || 'Failed to update product.';
        this.isSuccess = false;
      }
    });
  }
}
