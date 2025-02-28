import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-product',
  standalone: false,
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent {
  productForm: FormGroup;
  message: string = '';
  isSuccess: boolean = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      wholesalePrice: [0, [Validators.required, Validators.min(0)]],
      retailPrice: [0, [Validators.required, Validators.min(0)]],
      quantity: [0, [Validators.required, Validators.min(0)]]
    });
  }

  addProduct(): void {
    if (this.productForm.invalid) {
      this.message = 'Please fill all fields correctly.';
      this.isSuccess = false;
      return;
    }

    this.authService.addProduct(this.productForm.value).subscribe({
      next: () => {
        this.message = 'Product added successfully!';
        this.isSuccess = true;
        setTimeout(() => this.router.navigate(['/home']), 2000); // Redirect after 2s
      },
      error: (err) => {
        this.message = err.error || 'Failed to add product.';
        this.isSuccess = false;
      }
    });
  }
}
