import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-stats',
  standalone: false,
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.css'
})
export class StatsComponent implements OnInit {
  isAdmin: boolean = false;
  frequentPurchases: any[] = [];
  recentPurchases: any[] = [];
  mostProfitable: any[] = [];
  mostPopular: any[] = [];
  errorMessage: string = '';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.checkAdminStatus();
    this.loadStats();
  }

  checkAdminStatus() {
    this.isAdmin = localStorage.getItem('role') === 'ADMIN';
  }

  loadStats() {
    if (this.isAdmin) {
      this.authService.getMostProfitable().subscribe(
        response => { this.mostProfitable = response; },
        error => { this.errorMessage = 'Error loading profitable products.'; }
      );
      this.authService.getMostPopular().subscribe(
        response => { this.mostPopular = response; },
        error => { this.errorMessage = 'Error loading popular products.'; }
      );
    } else {
      this.authService.getFrequentPurchases().subscribe(
        response => { this.frequentPurchases = response; },
        error => { this.errorMessage = 'Error loading frequent purchases.'; }
      );
      this.authService.getRecentPurchases().subscribe(
        response => { this.recentPurchases = response; },
        error => { this.errorMessage = 'Error loading recent purchases.'; }
      );
    }
  }
}
