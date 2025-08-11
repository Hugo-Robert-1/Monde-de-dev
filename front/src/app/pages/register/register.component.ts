import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/features/auth/interfaces/loginRequest.interface';
import { RegisterRequest } from 'src/app/features/auth/interfaces/registerRequest.interface';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { strongPasswordValidator } from 'src/app/features/auth/validators/strongPasswordValidator';
import { SessionInformation } from 'src/app/features/sessions/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/features/sessions/services/session.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  public onError = false;
  
    public form = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      username: [
        '',
        [
          Validators.required,
          Validators.min(3)
        ]
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.min(8),
          strongPasswordValidator
        ]
      ]
    });
  
    constructor(private authService: AuthService,
                private fb: FormBuilder,
                private router: Router,
                private sessionService: SessionService) {
    }
  
    public submit(): void {
      const registerRequest = this.form.value as RegisterRequest;
      this.authService.register(registerRequest).subscribe({
        next: (response: SessionInformation) => {
          this.sessionService.logIn(response);
          this.router.navigate(['/posts']);
        },
        error: error => this.onError = true,
      });
    }

}
