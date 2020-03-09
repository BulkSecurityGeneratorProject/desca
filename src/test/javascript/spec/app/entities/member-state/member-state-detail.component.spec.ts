/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { MemberStateDetailComponent } from '../../../../../../main/webapp/app/entities/member-state/member-state-detail.component';
import { MemberStateService } from '../../../../../../main/webapp/app/entities/member-state/member-state.service';
import { MemberState } from '../../../../../../main/webapp/app/entities/member-state/member-state.model';

describe('Component Tests', () => {

    describe('MemberState Management Detail Component', () => {
        let comp: MemberStateDetailComponent;
        let fixture: ComponentFixture<MemberStateDetailComponent>;
        let service: MemberStateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MemberStateDetailComponent],
                providers: [
                    MemberStateService
                ]
            })
            .overrideTemplate(MemberStateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MemberStateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MemberStateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MemberState(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.memberState).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
