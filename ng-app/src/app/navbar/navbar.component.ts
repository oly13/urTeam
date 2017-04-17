import { Component } from '@angular/core';
import { Router} from '@angular/router';
import { LoginService } from '../login/login.service';
import { User } from '../user/user.model';

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html'
})
export class NavbarComponent {

    userLogged: User;

    constructor(private sessionService: LoginService, private router: Router) {
        this.userLogged = sessionService.userLogged;
  }
}