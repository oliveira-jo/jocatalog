import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { NgIf } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
    NgIf
  ],
  providers: [
    AuthService
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent implements OnInit {

  pageTitle: string = 'Login';
  errorMessage: string = '';
  loginForm!: FormGroup;
  formMode!: string;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.loginForm = this.formBuilder.group({
      username: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
      password: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]),
    });
  }

  login() {
    if (this.loginForm.invalid) return;
    this.authService.login(this.loginForm.value.username, this.loginForm.value.password).subscribe({
      next: () => {
        this.router.navigate(['/products-painel']);
      },
      error: (err) => {
        this.errorMessage = 'Invalid credentials';
      }
    });
  }

  closeAlert() {
    this.errorMessage = '';
    this.createForm();
  }

}
