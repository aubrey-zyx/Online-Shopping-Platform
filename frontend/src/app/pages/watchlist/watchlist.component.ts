import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-watchlist',
  standalone: false,
  templateUrl: './watchlist.component.html',
  styleUrl: './watchlist.component.css'
})
export class WatchlistComponent implements OnInit {
  watchlist: any[] = [];
  errorMessage: string = '';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.loadWatchlist();
  }

  loadWatchlist() {
    this.authService.getWatchlistProducts().subscribe(
      response => {
        this.watchlist = response;
      },
      error => {
        console.error('Error adding product:', error);
        this.errorMessage = 'Error loading watchlist.';
      }
    );
  }

  removeFromWatchlist(productId: number) {
    this.authService.removeFromWatchlist(productId).subscribe(
      () => {
        alert('Product removed from watchlist!');
        this.loadWatchlist();
      },
      error => {
        console.error('Error removing product:', error);
        alert('Failed to remove product from watchlist.');
      }
    );
  }
}
