import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { InternationalStandard } from './international-standard.model';
import { InternationalStandardService } from './international-standard.service';

@Injectable()
export class InternationalStandardPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private internationalStandardService: InternationalStandardService

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
                this.internationalStandardService.find(id)
                    .subscribe((internationalStandardResponse: HttpResponse<InternationalStandard>) => {
                        const internationalStandard: InternationalStandard = internationalStandardResponse.body;
                        this.ngbModalRef = this.internationalStandardModalRef(component, internationalStandard);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.internationalStandardModalRef(component, new InternationalStandard());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    internationalStandardModalRef(component: Component, internationalStandard: InternationalStandard): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.internationalStandard = internationalStandard;
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
