import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';

@Injectable()

export class LoaderService {
    public static isActive = true;

    isLoading = new Subject<boolean>();
    show() {
        if (LoaderService.isActive) {
            this.isLoading.next(true);
        }

    }
    hide() {
        if (LoaderService.isActive) {
            this.isLoading.next(false);
        }
    }
}
