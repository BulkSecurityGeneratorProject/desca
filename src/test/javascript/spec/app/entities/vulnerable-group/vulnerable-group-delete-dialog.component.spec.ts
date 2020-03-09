/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { VulnerableGroupDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group-delete-dialog.component';
import { VulnerableGroupService } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group.service';

describe('Component Tests', () => {

    describe('VulnerableGroup Management Delete Component', () => {
        let comp: VulnerableGroupDeleteDialogComponent;
        let fixture: ComponentFixture<VulnerableGroupDeleteDialogComponent>;
        let service: VulnerableGroupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [VulnerableGroupDeleteDialogComponent],
                providers: [
                    VulnerableGroupService
                ]
            })
            .overrideTemplate(VulnerableGroupDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VulnerableGroupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VulnerableGroupService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
