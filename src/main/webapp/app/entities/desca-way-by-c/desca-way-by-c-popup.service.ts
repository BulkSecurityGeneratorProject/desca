import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DescaWayByC } from './desca-way-by-c.model';
import { DescaWayByCService } from './desca-way-by-c.service';

@Injectable()
export class DescaWayByCPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private descaWayByCService: DescaWayByCService

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
                this.descaWayByCService.find(id)
                    .subscribe((descaWayByCResponse: HttpResponse<DescaWayByC>) => {
                        const descaWayByC: DescaWayByC = descaWayByCResponse.body;
                        this.ngbModalRef = this.descaWayByCModalRef(component, descaWayByC);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.descaWayByCModalRef(component, new DescaWayByC());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    descaWayByCModalRef(component: Component, descaWayByC: DescaWayByC): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.descaWayByC = descaWayByC;
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
