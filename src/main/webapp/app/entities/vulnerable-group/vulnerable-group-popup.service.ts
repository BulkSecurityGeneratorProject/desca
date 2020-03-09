import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { VulnerableGroup } from './vulnerable-group.model';
import { VulnerableGroupService } from './vulnerable-group.service';

@Injectable()
export class VulnerableGroupPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private vulnerableGroupService: VulnerableGroupService

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
                this.vulnerableGroupService.find(id)
                    .subscribe((vulnerableGroupResponse: HttpResponse<VulnerableGroup>) => {
                        const vulnerableGroup: VulnerableGroup = vulnerableGroupResponse.body;
                        this.ngbModalRef = this.vulnerableGroupModalRef(component, vulnerableGroup);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.vulnerableGroupModalRef(component, new VulnerableGroup());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    vulnerableGroupModalRef(component: Component, vulnerableGroup: VulnerableGroup): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.vulnerableGroup = vulnerableGroup;
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
