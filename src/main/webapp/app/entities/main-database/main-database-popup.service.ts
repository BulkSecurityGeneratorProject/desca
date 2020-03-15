import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { MainDatabase } from './main-database.model';
import { MainDatabaseService } from './main-database.service';

@Injectable()
export class MainDatabasePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private mainDatabaseService: MainDatabaseService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.mainDatabaseService.find(id)
                    .subscribe((mainDatabaseResponse: HttpResponse<MainDatabase>) => {
                        const mainDatabase: MainDatabase = mainDatabaseResponse.body;
                        this.ngbModalRef = this.mainDatabaseModalRef(component, mainDatabase);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.mainDatabaseModalRef(component, new MainDatabase());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    mainDatabaseModalRef(component: Component, mainDatabase: MainDatabase): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.mainDatabase = mainDatabase;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
