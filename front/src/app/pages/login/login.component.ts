import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/features/auth/interfaces/loginRequest.interface';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { SessionInformation } from 'src/app/features/sessions/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/features/sessions/services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  public onError = false;

  public form = this.fb.group({
    identifier: [
      '',
      [
        Validators.required,
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.min(8)
      ]
    ]
  });

  constructor(private authService: AuthService,
              private fb: FormBuilder,
              private router: Router,
              private sessionService: SessionService) {
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.authService.login(loginRequest).subscribe({
      next: (response: SessionInformation) => {
        this.sessionService.logIn(response);
        this.router.navigate(['/posts']);
      },
      error: error => this.onError = true,
    });
  }

}
