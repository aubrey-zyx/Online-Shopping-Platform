import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  backendErrors: any = {}; // Store backend validation errors

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    this.authService.register(this.registerForm.value).subscribe(
      response => {
        console.log('Registration successful', response);
        this.backendErrors = {}; // Clear errors on success
        this.router.navigate(['/login']);
      },
      error => {
        if (error.status === 400) {
          this.backendErrors = error.error; // Assign backend errors to display in the form
        } else {
          console.error('Unexpected error:', error);
        }
      }
    );
  }
}
